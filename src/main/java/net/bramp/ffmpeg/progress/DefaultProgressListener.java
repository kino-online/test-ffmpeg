package net.bramp.ffmpeg.progress;

import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.util.concurrent.TimeUnit;

public class DefaultProgressListener implements ProgressListener {
	public DefaultProgressListener(FFmpegProbeResult in) {
		this.duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
	}

	double duration_ns;

	@Override
	public void progress(Progress progress) {
		double percentage = progress.out_time_ns / duration_ns;
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
}
