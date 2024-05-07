тестирование ffmpeg, запуск с разными опциями

цель: java методы для сборки динамически заполняемых опции к ffmpeg для нужд кинотеатра - конвертации входного видео в формат hls с несколькими дорожками аудио и субтитров

здесь используется https://github.com/orthlus/aelaort-ffmpeg

бинарники ffmpeg поставляются из maven зависимости из https://github.com/bytedeco/javacv

идея и чатстично код обертки над ffmpeg и ffprobe взяты из https://github.com/bramp/ffmpeg-cli-wrapper
