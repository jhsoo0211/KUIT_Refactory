# Source manifest

2026-08-15에 아래 원본 ref의 파일 트리를 경로별로 가져왔습니다. 기존 저장소의 과거 Git 객체를 새 원격에 중복 배포하지 않고, 원본 URL·ref·전체 commit SHA를 교차 확인 기준으로 사용합니다.

| 대상 경로 | 원본 저장소 | 원본 ref | 원본 commit |
|---|---|---|---|
| `coursework/week01-ui-basics` | [`jhsoo0211/KUIT5_Android_week1`](https://github.com/jhsoo0211/KUIT5_Android_week1) | `jhsoo0211/week1` | `09a8c42988b09852c2cd179eecfae8b046969700` |
| `coursework/week02-compose-layout` | [`jhsoo0211/KUIT5_Android`](https://github.com/jhsoo0211/KUIT5_Android) | `jhsoo0211/week2` | `eb8c541382d8e7b4e3fc7d4540bf56955246671b` |
| `coursework/week03-state-ui` | [`jhsoo0211/KUIT5_Android`](https://github.com/jhsoo0211/KUIT5_Android) | `jhsoo0211/week3` | `c5d5c95058b0fb4ac8ceb89e8a6f921b4b0e9b5e` |
| `coursework/week04-compose-navigation` | [`jhsoo0211/KUIT5_Android`](https://github.com/jhsoo0211/KUIT5_Android) | `jhsoo0211/week4` | `b3f8e094e4e22597b79f57e205cb558dd2f8465a` |
| `coursework/week05-network-basics` | [`jhsoo0211/KUIT5_Android_Api`](https://github.com/jhsoo0211/KUIT5_Android_Api) | `jhsoo0211/week5` | `7a7b5450e725e76d0f6822b64faf5f21d9e6b85b` |
| `coursework/week06-api-coroutines` | [`jhsoo0211/KUIT5_Android-API`](https://github.com/jhsoo0211/KUIT5_Android-API) | `jhsoo0211/week6` | `97f6d324e421b9b4f2e020d933d7f5dd2aba89a1` |
| `coursework/week07-repository-uistate` | [`jhsoo0211/KUIT5_Android-API`](https://github.com/jhsoo0211/KUIT5_Android-API) | `jhsoo0211/week7` | `7cd25afedf58a9197e1b6a7ebca69913b5e76ba9` |
| `coursework/week08-datastore` | [`jhsoo0211/KUIT5_Android-API`](https://github.com/jhsoo0211/KUIT5_Android-API) | `jhsoo0211/week8` | `a36d2c18c7e4be2ae917cb5c8711ce2d58d13a61` |
| `coursework/week09-network-architecture` | [`jhsoo0211/KUIT5_Android-API`](https://github.com/jhsoo0211/KUIT5_Android-API) | `jhsoo0211/week9` | `90479cd8ed721acd348460d5bf3fdafe1495b9c9` |
| `reference/project-scaffold` | [`jhsoo0211/KUIT-project-setting`](https://github.com/jhsoo0211/KUIT-project-setting) | `1-project-setting` | `3c4bb426660d9e74773e48cd42e6b69ba6de5a57` |
| `projects/moru-android` | [`KUIT-MORU/KUIT_MORU_Android`](https://github.com/KUIT-MORU/KUIT_MORU_Android) | `master` | `9a6a7d1ff7a0070e5e860ba7b9336c1ceaab643f` |

MORU는 import 직후 포트폴리오용 보안·빌드·테스트 개선을 적용했으므로 현재 tree가 원본 commit과 같지 않습니다. 원본 과거 tree에는 보안상 재배포하면 안 되는 값이 포함되어 있어 Git object/history는 새 원격에 미러링하지 않았습니다. 팀 원본 저장소는 변경하지 않았습니다.

주차별 학습 소스와 scaffold 구현은 import 후 수정하지 않았습니다. 중첩 Git metadata, IDE·local 설정, build/release 산출물처럼 포트폴리오 소스가 아닌 항목은 게시 대상에서 제외했습니다. 또한 경로를 monorepo 하위로 옮겼으므로 통합 commit의 tree SHA 자체는 원본 root tree SHA와 다릅니다.
