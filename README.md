# Andlytics

이 저장소에는 Andlytics Android 앱의 소스 코드가 들어 있습니다.

<a href="https://play.google.com/store/apps/details?id=com.github.andlyticsproject" alt="Google Play에서 다운로드">
<img src="http://www.android.com/images/brand/android_app_on_play_large.png">
</a>

![](https://lh4.ggpht.com/ckVylBqx0sS1b-KW99qkg7NYuDNRGGstnZKsw-qe3TnpUOH4em5cH-8QuPXs2NQj9Nou=w705)

버그나 기능 요청을 보고하고 알려진 문제 목록을 보려면 [문제](https://github.com/AndlyticsProject/andlytics/issues) 섹션을 참조하세요.

### 알파/베타 버전
G+ 커뮤니티에 가입하고 테스터로 등록하여 최신 알파/베타 버전을 받으세요.

https://plus.google.com/communities/106533634605835980810

https://play.google.com/apps/testing/com.github.andlyticsproject

### 야간 빌드

* http://andlytics.schoentoon.com/andlytics.apk

http://andlytics.schoentoon.com/latest_version에서 빌드된 커밋을 확인할 수 있으며 매일 자정(GMT +1) 직후에 업데이트됩니다.

## 라이선스

저작권 2012 Timelappse
저작권 2012 Andlytics Project

Apache 라이선스 버전 2.0(이하 "라이선스")에 따라 라이선스가 부여됨.
라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
라이선스 사본은 다음에서 얻을 수 있습니다.

http://www.apache.org/licenses/LICENSE-2.0

해당 법률에 의해 요구되거나 서면으로 동의하지 않는 한, 라이선스에 따라 배포되는 소프트웨어는 명시적이든 묵시적이든 어떠한 종류의 보증이나 조건 없이 "있는 그대로" 배포됩니다.
라이선스에 따른 권한 및 제한을 규정하는 특정 언어는 라이선스를 참조하세요.

# 설명

Andlytics는 Google Play 개발자 콘솔에서 통계를 수집합니다. Google Play에 게시된 모든 Android 앱에 대한 활성 설치, 총 설치, 평가 및 댓글을 추적할 수 있습니다.

Andlytics는 수익, 요청 및 노출을 포함한 AdMob 통계도 수집할 수 있습니다. 변경 사항이 발생할 때 통계 및 알림의 백그라운드 동기화를 지원합니다.

Google은 통계를 수집하기 위한 공개 API를 제공하지 않습니다. 따라서 Google에서 사이트를 변경하면 Andlytics가 작동을 멈출 수 있습니다. 그런 경우 따라잡으려고 노력하는 동안 기다려 주시기 바랍니다.

Andlytics는 오픈 소스이므로 GitHub을 통해 자유롭게 기여하세요.

Andlytics는 어떤 방식으로든 Google과 연관이 없습니다.

"Google Apps for Business" 및 다중 연결 개발자 계정은 아직 지원되지 않습니다. 이러한 계정 중 하나가 있는 경우 GitHub을 통해 문의하여 지원을 추가하세요.

일부 기기에서 AdMob 계정이 계속 사라지는데, Andlytics를 SD 카드에서 내부 저장소로 이동하면 이 문제가 해결될 수 있습니다.

Google 계정에 연결된 AdMob 계정의 경우 계정 정보 페이지 http://www.admob.com/my_account/account_info에서 API 비밀번호를 찾을 수 있습니다.

키워드: 개발자 콘솔, 개발자 도구, Android 개발자

## 기여

이 저장소를 포크하고
[풀 요청](https://github.com/AndlyticsProject/andlytics/pulls)을 사용하여 다시 기여하세요.

'마스터' 브랜치는 항상 안정적이고 테스트되었으며 사용할 준비가 된 상태여야 Google에서 'API'를 중단할 때 빠르게 수정 사항을 구현할 수 있습니다. 따라서 모든 풀 리퀘스트는 ['dev'](https://github.com/AndlyticsProject/andlytics/tree/dev) 브랜치를 기반으로 하고 이를 대상으로 해야 합니다.

코드를 제출할 때는 주변 코드와 일관성을 유지하도록 노력하세요. 일반적인 규칙은 다음과 같습니다.
1. 공백이 아닌 탭을 사용하세요.
2. 한 줄로 하면 더 보기 좋지 않은 한 최대 줄 길이는 100줄로 하세요.

크거나 작은 기여, 주요 기능, 버그 수정, 단위/통합 테스트는 환영하고 감사하지만, 철저히 검토하고 논의할 것입니다.

번역은 [crowdin.net](http://crowdin.net/project/andlytics)을 통해 수행해야 합니다.

## 예제
git clone https://github.com/<git_account>/andlytics.git

cd <directory>

git remote add upstream https://github.com/AndlyticsProject/andlytics

git checkout master
git pull upstream master && git push origin master
git checkout -b readme-update

변경 사항을 제출하기 위한 풀 리퀘스트 만들기

### 자체 버전 빌드

패키지 이름과 서명 인증서 해시를 Google API 콘솔에 추가합니다. 첨부된 이미지 참조:
![api-console](https://cloud.githubusercontent.com/assets/1053078/25211216/18b2dba4-25bf-11e7-9b5f-cf7a53776bc7.png)

## 소셜미디어

<a href="https://plus.google.com/103867882342055020019" rel="publisher">Google+에서 찾아보세요</a>
