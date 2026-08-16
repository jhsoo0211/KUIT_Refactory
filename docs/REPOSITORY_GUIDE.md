# Repository guide

이 저장소는 흩어져 있던 KUIT Android 학습 결과와 팀 프로젝트 MORU를 하나의 검토 경로로 묶은 포트폴리오입니다. `coursework`와 `reference`는 당시 상태를 보여 주는 기록이고, `projects/moru-android`는 이후에도 개선하는 대표 프로젝트입니다.

## Directory map

```text
KUIT_Refactory/
├─ README.md                         # 포트폴리오 입구
├─ coursework/
│  ├─ README.md                     # 1~9주차 학습 목차
│  └─ week01-... ~ week09-.../      # 각각 독립된 Android/Gradle 프로젝트
├─ projects/
│  └─ moru-android/                 # 대표 프로젝트, 독립된 단일 :app 프로젝트
├─ reference/
│  └─ project-scaffold/             # KUIT 프로젝트 시작용 참고 스냅샷
├─ docs/
│  ├─ REPOSITORY_GUIDE.md           # 현재 문서
│  ├─ MORU_ARCHITECTURE.md          # MORU 구조와 개발 절차
│  ├─ CONTRIBUTIONS.md              # 팀 결과와 개인 기여 근거
│  └─ SOURCE_MANIFEST.md            # 원본 URL, ref, commit SHA
├─ .github/workflows/
│  └─ moru-android.yml              # MORU 전용 자동 검증
└─ NOTICE.md                        # 출처와 라이선스 경계
```

저장소 루트에는 공통 Gradle 설정이 없습니다. 각 주차 폴더와 MORU 폴더에서 해당 프로젝트의 wrapper를 사용해야 하며, 루트 명령 하나로 전체를 빌드하는 구성은 아닙니다.

## Learning path

| 단계 | 경로 | 확인할 성장 포인트 |
|---|---|---|
| Compose 기초 | `coursework/week01-ui-basics` ~ `week04-compose-navigation` | UI 재현, 상태 기반 컴포넌트, 화면 이동과 back stack |
| 네트워크 기초 | `coursework/week05-network-basics` ~ `week06-api-coroutines` | Retrofit 요청, Coroutine, 공통 응답과 오류 UI |
| 상태·데이터 구조화 | `coursework/week07-repository-uistate` ~ `week09-network-architecture` | Repository, ViewModel/UiState, DataStore, Hilt |
| 팀 제품 적용 | `projects/moru-android` | Compose 화면, 서버 계약, 로컬 상태, Navigation, 협업과 후속 안정화 |

각 과제의 원본 브랜치는 [`coursework/README.md`](../coursework/README.md), 가져온 시점의 전체 commit SHA는 [`SOURCE_MANIFEST.md`](SOURCE_MANIFEST.md)에서 확인할 수 있습니다.

## Preservation and maintenance policy

| 구역 | 성격 | 변경 원칙 |
|---|---|---|
| `coursework/` | 당시 제출 상태를 보존한 학습 스냅샷 | 포맷 일괄 수정이나 최신 구조로의 재작성보다 원본성 유지 |
| `reference/` | 프로젝트 시작용 참고 자료 | 제품 코드와 섞지 않고 출처 기준점 유지 |
| `projects/moru-android/` | 팀 결과를 기반으로 한 개인 포트폴리오 사본 | 보안, 재현 가능한 빌드, 테스트, 구조를 지속적으로 개선 |
| `docs/`, 루트 문서 | 검증과 탐색을 위한 설명 | 코드와 원본 기준점이 바뀌면 함께 갱신 |

MORU의 후속 변경은 팀 원본 저장소가 아니라 이 개인 사본에만 적용합니다. MORU Server는 별도 팀 저장소이며 이 저장소에 복제하지 않습니다. 출처와 권리 경계는 [`NOTICE.md`](../NOTICE.md)를 따릅니다.

## Recommended review order

1. 루트 [`README.md`](../README.md)에서 프로젝트 목표와 검증 결과를 확인합니다.
2. [`projects/moru-android/README.md`](../projects/moru-android/README.md)에서 기능과 실행 조건을 확인합니다.
3. [`MORU_ARCHITECTURE.md`](MORU_ARCHITECTURE.md)에서 실제 계층, 남은 결합, 개발 절차를 확인합니다.
4. [`CONTRIBUTIONS.md`](CONTRIBUTIONS.md)에서 팀 결과와 개인 기여를 구분합니다.
5. [`SOURCE_MANIFEST.md`](SOURCE_MANIFEST.md)에서 모든 가져오기 기준점을 검증합니다.

## Legacy repository retirement

기존 개인 KUIT 저장소는 이 통합 내용이 기본 브랜치에 반영되고 MORU CI가 통과한 뒤 정리하는 것이 안전합니다. 삭제 대신 기존 저장소 설명과 README에 이 저장소의 통합 경로를 남기고 archive하면 과거 링크와 commit 기록을 보존할 수 있습니다. 팀 MORU Android와 MORU Server는 이 정리 대상이 아닙니다.
