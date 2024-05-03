import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static art.aelaort.FFmpegFactory.ffmpegBuilder;
import static art.aelaort.FFmpegRunUtils.probe;
import static art.aelaort.FFmpegRunUtils.run;

public class Main {
	public static void main(String[] args) {
		String now = LocalDateTime.now().toString()
				.replace("T", "-")
				.replaceAll(":", "-");
		Path dir = Path.of("D:/temp/house-test/result/" + now);

		String filename1 = "D:/temp/Enola.Holmes.2.2022.1080p.NewComers.mkv";
		String filename2 = "D:/temp/Shinseiki Evangelion 01 [AC-3 RUS] [ASS RUS] [AAC JPN] [HEVC 1920x1080] [BDRip].mkv";
		FFmpegProbeResult in = probe(filename2);

		String ffmpegArgs = "-c copy -map 0 -segment_time 00:00:10 -f segment -reset_timestamps 1 output-%d.ts";
		FFmpegBuilder ffmpegBuilder = ffmpegBuilder()
				.addInput(filename2)
//				.addExtraArgs(ffmpegArgs)
				.addOutput(filename2.replace(".mkv", ".mp4"))
				.done();
		run(ffmpegBuilder, new ProgressListener() {

			// Using the FFmpegProbeResult determine the duration of the input
			final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

			@Override
			public void progress(Progress progress) {
				double percentage = progress.out_time_ns / duration_ns;

				// Print out interesting information about the progress
				System.out.printf(
						"[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx%n",
						percentage * 100,
						progress.status,
						progress.frame,
						FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
						progress.fps.doubleValue(),
						progress.speed
				);
			}
		}).run();
		System.out.println(dir + " finished");
	}
}
