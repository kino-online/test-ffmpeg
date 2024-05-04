import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.DefaultProgressListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static art.aelaort.FFmpegFactory.ffmpegBuilder;
import static art.aelaort.FFmpegRunUtils.probe;
import static art.aelaort.FFmpegRunUtils.run;

public class Main {
	public static void main(String[] args) throws IOException {
		String root = "D:/temp/house-test";
		String srcFile = root + "/House-S07E23.mkv";
		Path dir = Path.of(root + "/result/" + now());
		Files.createDirectory(dir);

		test1(srcFile, dir);

		System.out.println(dir + " finished");
	}

	private static void test1(String srcFile, Path dir) {
		FFmpegProbeResult inProbe = probe(srcFile);

		String ffmpegArgs = "";

		FFmpegBuilder ffmpegBuilder = ffmpegBuilder()
				.addInput(srcFile)
				.addExtraArgs(ffmpegArgs)
				.addOutput(dir + "/index.m3u8")
				.done();
		run(ffmpegBuilder, new DefaultProgressListener(inProbe)).run();
	}

	private static String now() {
		return LocalDateTime.now().toString().replace("T", "-").replaceAll(":", "-");
	}
}
