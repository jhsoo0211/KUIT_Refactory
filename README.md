# KUIT Android Portfolio

[![MORU Android CI](https://github.com/jhsoo0211/KUIT_Refactory/actions/workflows/moru-android.yml/badge.svg)](https://github.com/jhsoo0211/KUIT_Refactory/actions/workflows/moru-android.yml)

<p align="center">
  <img src="projects/moru-android/app/src/main/ic_launcher-playstore.png" width="144" alt="MORU app icon" />
</p>

KUIT 5기에서 진행한 Android 학습 기록과 4인 팀 프로젝트 **MORU**를 한곳에 정리한 포트폴리오 저장소입니다. 최종 결과물은 먼저, 주차별 과제는 성장 과정으로 배치했습니다.

## MORU

> 다른 사람의 좋은 루틴을 발견하고, 내 루틴으로 이어 가는 Android 앱

[MORU Android](projects/moru-android)는 Jetpack Compose 기반의 루틴 탐색·관리 서비스입니다. 루틴 피드, 검색, 팔로우, 스크랩, 내 루틴 관리, 알림 흐름을 하나의 앱에서 연결합니다.

- **팀 구성:** Android 4인 협업
- **개인 기여:** My Routine, Routine Feed 상세, 검색·프로필·팔로우, 공통 Routine 모델과 Navigation, 서버 연동 및 FCM 흐름
- **기여 근거:** 작성자 명의로 병합된 PR 16개 — [상세 보기](docs/CONTRIBUTIONS.md)
- **기술:** Kotlin, Jetpack Compose, MVVM, Hilt, Retrofit, OkHttp, Kotlin Serialization, DataStore, Firebase

### 포트폴리오 정비에서 개선한 부분

- 모든 인증서와 호스트를 신뢰하던 HTTP 클라이언트를 제거하고 표준 TLS 검증으로 복구
- 하드코딩 토큰 fallback과 민감한 인증·FCM·사용자 작성 내용 로그 제거
- `local.properties`와 Firebase 파일이 없는 깨끗한 clone도 컴파일할 수 있도록 설정 분리
- 존재하지 않는 경로로 이동하던 FCM 루틴 상세 deep link 수정
- 로그인·회원가입 입력 규칙을 공통 validator로 분리하고 단위 테스트 추가
- 생성물·임시 파일 제거, ignore 규칙과 GitHub Actions 검증 추가

이 변경은 팀 원본 저장소가 아니라 이 개인 포트폴리오 사본에만 적용했습니다.

### 검증 결과 (2026-08-16)

포트폴리오 정비가 끝난 동일한 소스에서 `testDebugUnitTest`, `lintDebug`, `assembleDebug`를 강제 재실행했습니다. 인증 입력 검증 테스트 3개가 모두 통과했고, Lint는 오류 0건, Debug APK 빌드는 성공했습니다. 실제 서버·Firebase·실기기 E2E는 이 결과에 포함하지 않습니다.

## Learning timeline

| 주차 | 핵심 주제 | 결과물 |
|---|---|---|
| 01 | Compose UI 기초와 프로필 카드 | [week01-ui-basics](coursework/week01-ui-basics) |
| 02 | 레이아웃과 자산 구성 | [week02-compose-layout](coursework/week02-compose-layout) |
| 03 | 상태 기반 UI와 컴포넌트 | [week03-state-ui](coursework/week03-state-ui) |
| 04 | Navigation과 back stack | [week04-compose-navigation](coursework/week04-compose-navigation) |
| 05 | Retrofit GET·POST·DELETE | [week05-network-basics](coursework/week05-network-basics) |
| 06 | Coroutine과 공통 응답 처리 | [week06-api-coroutines](coursework/week06-api-coroutines) |
| 07 | Repository, ViewModel, UiState | [week07-repository-uistate](coursework/week07-repository-uistate) |
| 08 | DataStore 기반 로컬 상태 | [week08-datastore](coursework/week08-datastore) |
| 09 | Hilt 기반 의존성 주입 | [week09-network-architecture](coursework/week09-network-architecture) |

각 폴더는 당시 제출 상태를 보존한 독립 Android 프로젝트입니다. 자세한 학습 흐름과 원본 링크는 [coursework/README.md](coursework/README.md)에서 확인할 수 있습니다.

## Repository map

```text
.
├─ projects/moru-android/       # 대표 팀 프로젝트와 후속 개선
├─ coursework/week01-09.../     # 주차별 독립 학습 스냅샷
├─ reference/project-scaffold/  # KUIT 프로젝트 시작 템플릿
├─ docs/                        # 기여 근거와 출처 기록
└─ .github/workflows/           # MORU 자동 검증
```

- [MORU 실행 및 구조](projects/moru-android/README.md)
- [개인 기여 근거](docs/CONTRIBUTIONS.md)
- [원본 저장소·브랜치·SHA](docs/SOURCE_MANIFEST.md)
- [저작권 및 출처 고지](NOTICE.md)

## Scope

이 저장소에는 KUIT 학습 결과와 MORU Android만 포함합니다. MORU Server는 팀의 별도 저장소이며 이곳에 복제하지 않았습니다. 원본 프로젝트에는 별도 라이선스가 명시되어 있지 않으므로 이 통합 역시 새로운 오픈소스 라이선스를 부여하지 않습니다.
