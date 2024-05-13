import art.aelaort.ffmpeg.FFmpeg;
import art.aelaort.ffprobe.FFprobe;
import art.aelaort.ffprobe.models.FFprobeResult;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
	public static void main(String[] args) throws IOException {
		String root = "D:/temp/house-test";
		String srcFile = root + "/House-S07E23.mkv";
		Path dir = Path.of(root + "/result/" + now());
		Files.createDirectory(dir);

		System.out.println(dir + " created");

		test2(srcFile, dir);

		System.out.println(dir + " finished");
		System.out.println("dir named save to clipboard");
		copyToClipboard(now);
	}

	private static void test2(String srcFile, Path dir) {
		FFprobeResult probe = FFprobe.probe(srcFile);
		String frameRate = String.valueOf(probe.getVideoStream().getRFrameRate() * 2);
		FFmpeg ffmpeg = FFmpeg.builder()
				.printOnlyError(false)
				.input(srcFile)
				.inputDuration("120")
				.args("-preset", "slow")
				.args("-g", frameRate)
				.args("-sc_threshold", "0")

				.map("0:v:0")
				.map("0:a:0")
//				.map("0:v:0")
				.map("0:a:1")
//				.map("0:s:0")
//				.map("0:s:1")

				.args("-c:v", "copy")
//				.args("-c:a", "copy")
				.args("-c:a", "mp3")
//				.args("-c:s", "webvtt")

//				.args("-strftime", "1")
				.args("-f", "hls")
				.args("-hls_playlist_type", "vod")
				.args("-hls_flags", "independent_segments")
				.args("-hls_segment_type", "mpegts")
				.args("-master_pl_name", "master.m3u8")
//				.args("-var_stream_map", "\"v:0,a:0,s:1 v:0,a:1,s:0\"")
				.args("-var_stream_map", "\"v:0,a:0 a:1\"")

				.args("-hls_time", "30")
				.args("-hls_segment_filename", dir + "/index_%v/data_%d.ts")

				.output(dir + "/index_%v/index.m3u8")
				.build();

		saveCommandToBatch(ffmpeg);
	}

	/**
	 * из одного файла генерация двух потоков с разными аудио. <p>
	 * аудио внутри видео файлов, то есть видео дублируется. аудио кодируется в mp3 или ac3 <p>
	 * videojs не воспроизводит это аудио
	 */
	private static void test1(String srcFile, Path dir) {
		FFprobeResult probe = FFprobe.probe(srcFile);
		String frameRate = String.valueOf(probe.getVideoStream().getRFrameRate() * 2);
		FFmpeg ffmpeg = FFmpeg.builder()
				.printOnlyError(false)
				.input(srcFile)
				.args("-preset", "slow")
				.args("-g", frameRate)
				.args("-sc_threshold", "0")

				.map("0:v:0")
				.map("0:a:0")
				.map("0:v:0")
				.map("0:a:1")
//				.map("0:s:0")
//				.map("0:s:1")

				.args("-c:v", "copy")
				.args("-c:a", "copy")
//				.args("-c:a", "mp3")
//				.args("-c:s", "webvtt")

//				.args("-strftime", "1")
				.args("-f", "hls")
				.args("-hls_playlist_type", "vod")
				.args("-hls_flags", "independent_segments")
				.args("-hls_segment_type", "mpegts")
				.args("-master_pl_name", "master.m3u8")
//				.args("-var_stream_map", "\"v:0,a:0,s:1 v:0,a:1,s:0\"")
				.args("-var_stream_map", "\"v:0,a:0 v:1,a:1\"")

				.args("-hls_time", "30")
				.args("-hls_segment_filename", dir + "/index_%v/data_%d.ts")

				.output(dir + "/index_%v/index.m3u8")
				.build();

		saveCommandToBatch(ffmpeg);
	}

	private static void saveCommandToBatch(FFmpeg ffmpeg) {
		try {
			String command = ffmpeg.command().replaceAll("%", "%%");
			Files.writeString(Path.of("ffmpeg.bat"), command);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String now() {
		return LocalDateTime.now().toString()
				.replaceAll(":", "_")
				.replaceAll("-", "_")
				.replace(".", "_");
	}

	private static void copyToClipboard(String s) {
		StringSelection stringSelection = new StringSelection(s);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}
