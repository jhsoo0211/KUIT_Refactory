# MORU contribution evidence

MORU Android는 [`jhsoo0211`](https://github.com/jhsoo0211), [`chohs4164`](https://github.com/chohs4164), [`adagioCHAN`](https://github.com/adagioCHAN), [`hyungyu-02`](https://github.com/hyungyu-02)가 함께 만든 4인 팀 프로젝트입니다. 아래에는 포트폴리오 소유자 `jhsoo0211` 명의로 원본 `master`에 병합된 PR 16개만 정리했습니다.

검증 기준은 팀 원본 `master@9a6a7d1ff7a0070e5e860ba7b9336c1ceaab643f`입니다. 아래 병합 commit은 모두 이 기준점의 ancestor임을 확인했습니다.

## 담당 영역

- My Routine 메인·상세·수정·스케줄 흐름
- Routine Feed 상세, 검색, 사용자 프로필, 팔로우·팔로워
- 공통 Routine 데이터 모델과 재사용 UI 컴포넌트
- 화면 간 Navigation과 ID 타입 정리
- 서버 API 연결, 알림·FCM 연동 마무리

## Merged pull requests

| PR | 병합일 | 병합 commit | PR에 기록된 범위 |
|---|---|---|---|
| [#3](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/3) | 2025-07-10 | `c4ed0b7` | Routine Feed UI, 공통 컴포넌트, 알림 진입 Navigation |
| [#16](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/16) | 2025-07-12 | `7ae343b` | My Routine 메인과 재사용 Dialog |
| [#19](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/19) | 2025-07-14 | `a56b458` | Routine 데이터 모델 통합 |
| [#21](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/21) | 2025-07-14 | `e6ca594` | Routine Feed 상세 UI와 chip style |
| [#25](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/25) | 2025-07-16 | `234e32a` | 루틴 상세 Navigation과 데이터 정리 |
| [#29](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/29) | 2025-07-18 | `e0ff646` | 팔로우 Navigation, 프로필 전달, 명칭 정리 |
| [#30](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/30) | 2025-07-20 | `4250794` | 검색 UI와 Navigation |
| [#36](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/36) | 2025-07-28 | `657b9b1` | My Routine 기능 구현 |
| [#43](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/43) | 2025-07-30 | `5eb605a` | My Routine 수정 로직 |
| [#46](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/46) | 2025-07-31 | `e705e55` | refactoring과 루틴 생성 공통 컴포넌트 |
| [#59](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/59) | 2025-08-08 | `2d1812e` | ID 자료형을 `Int`에서 `String`으로 변경 |
| [#78](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/78) | 2025-08-14 | `8835009` | Routine Feed와 이전 작업 통합 |
| [#82](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/82) | 2025-08-16 | `610d5cd` | Routine Feed·My Routine 서버 연결 |
| [#86](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/86) | 2025-08-17 | `7dbaea3` | My Routine·Routine Feed 화면 마무리 |
| [#87](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/87) | 2025-08-18 | `42dbf6d` | 알림, 사용자 표시, 입력 오류 수정 |
| [#91](https://github.com/KUIT-MORU/KUIT_MORU_Android/pull/91) | 2025-08-18 | `cb6bd57` | 회의 결과에 따른 문구 정리 |

PR 개수는 소유권 비율이나 단독 저작을 뜻하지 않습니다. 팀 결과와 개인 기여를 구분하기 위한 검증 가능한 근거로만 사용합니다.

## 2026 portfolio maintenance

과정 종료 후 개인 통합 사본에서 네트워크 보안, 빌드 재현성, 민감 로그, FCM deep link, 입력 검증 테스트, CI와 문서를 별도로 개선했습니다. 이 후속 작업은 위 팀 PR과 구분됩니다.
