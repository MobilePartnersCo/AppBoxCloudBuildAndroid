# 1. 베이스 이미지: JDK 17 (Android 빌드 표준)
FROM eclipse-temurin:17-jdk-jammy

# 2. 필수 패키지 설치 및 보안 업데이트
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends curl unzip zip git file imagemagick && \
    rm -rf /var/lib/apt/lists/*

# 3. 환경 변수 설정
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# 4. Android Command Line Tools 다운로드 및 구조 설정
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip && \
    unzip -q cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm cmdline-tools.zip

# 5. 라이선스 자동 동의
RUN yes | sdkmanager --licenses

# 6. 필수 SDK 패키지 설치
RUN sdkmanager "platform-tools" \
    "platforms;android-35" \
    "platforms;android-36" \
    "build-tools;35.0.0" \
    "build-tools;36.0.0"