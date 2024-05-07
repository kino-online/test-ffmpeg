import art.aelaort.ffmpeg.FFmpeg;
import art.aelaort.ffprobe.FFprobe;
import art.aelaort.ffprobe.models.FFprobeResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static java.util.List.of;

public class Main {
	public static void main(String[] args) throws IOException {
		String root = "D:/temp/house-test";
		String srcFile = root + "/House-S07E23.mkv";
		Path dir = Path.of(root + "/result/" + now());
		Files.createDirectory(dir);

		System.out.println(dir + " created");

		test1(srcFile, dir);

		System.out.println(dir + " finished");
	}

	private static void test1(String srcFile, Path dir) throws IOException {
		FFprobeResult probe = FFprobe.probe(srcFile);
		String frameRate = String.valueOf(probe.getVideoStream().getRFrameRate() * 2);
		FFmpeg ffmpeg = FFmpeg.builder()
				.printOnlyError(false)
				.input(srcFile)
				.args("-preset", "slow")
				.args("-g", frameRate)
				.args("-sc_threshold", "0")

				.map("0:0").map("0:1")
				.map("0:0").map("0:1")

				.args("-f", "hls")
				.args("-hls_playlist_type", "vod")
				.args("-hls_flags", "independent_segments")
				.args("-hls_segment_type", "mpegts")
				.args("-master_pl_name", "master.m3u8")

				.args("-hls_time", "30")
				.args("-hls_segment_filename", "index_%v/data%02d.ts")

				.output(dir + "/index_%v.m3u8")
				.build();

		Files.writeString(Path.of("ffmpeg.bat"), ffmpeg.command());
	}

	private static String now() {
		return LocalDateTime.now().toString().replace("T", "-").replaceAll(":", "-");
	}
}
