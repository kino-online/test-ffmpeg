import art.aelaort.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static art.aelaort.FFmpegRunUtils.probe;
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

	private static void test1(String srcFile, Path dir) {
		FFmpegProbeResult inProbe = probe(srcFile);

		FFmpeg.builder()
				.input(srcFile)
				.output((dir + "/index.m3u8"))
				.args(of("-acodec", "aac", "-vcodec", "h264"))
				.arg("-hls_time")
				.arg("120")
				.arg("")
				.printOnlyError(true)
				.build()
				.run();
	}

	private static String now() {
		return LocalDateTime.now().toString().replace("T", "-").replaceAll(":", "-");
	}
}
