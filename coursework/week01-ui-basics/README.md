# KUIT5_week1
# 저장소 Fork & Clone 하는 방법

## Branch 생성
[원본 레포지토리](https://github.com/Konkuk-KUIT/KUIT5_week1) 에서 github 이름의 branch 를 입력합니다.
Create branch 를 눌러 브랜치를 생성합니다.

<img src="https://github.com/user-attachments/assets/4040ec09-c8eb-4f58-b557-2656d98c7b7e" width="600"/>


## 저장소 Fork
1. [원본 레포지토리](https://github.com/Konkuk-KUIT/KUIT5_week1) 에서 **Fork** -> **Create a new fork** 를 클릭합니다.

<img src="https://github.com/user-attachments/assets/a268affd-c3cc-45a3-a008-b3e50c8e8e4a" width="600"/>

2. 그 후 다음 화면으로 넘어가 **Create fork** 를 클릭합니다.
   - 아무것도 설정하지 않아도 됩니다. ")

<img src="https://github.com/user-attachments/assets/2e64cc0c-e283-4878-a1a7-9e7c246570f1" width="600"/>

## Fork 된 저장소 확인 및 Branch 생성
1. 본인 **Github ID/KUIT5_Android_week1** 형식으로 좌측상단에 뜨는 지 확인합니다.
2. 아까처럼 **Branch** 를 생성하는데, 이번에는 본인 github 이름-week주차 형식으로 입력합니다.
   - ex) ikseong00/week1
   - Git Clone 한 다음에 브랜치 생성하셔도 됩니다! (이게 보통 권장되는 방식이에요.)

<img src="https://github.com/user-attachments/assets/ff03362a-1910-4d7c-8592-de187bfcc88d" width="600"/>

## Fork 된 저장소를 Git Clone

1. **Code** 클릭 -> **복사 아이콘** 클릭해서 깃허브 주소를 복사합니다. 


<img src="https://github.com/user-attachments/assets/9e13bdac-c0b1-49ce-bb5e-7159f4cfdfe5" width="600"/>

2. 작성하려는 디렉토리/폴더 에서 터미널 혹은 Git Bash 를 실행해주세요. 

<img src="https://github.com/user-attachments/assets/a11e0d73-51d6-44df-a811-cf34ad04078a" width="480"/>

3. 실행 후 다음과 같이 커맨드를 입력합니다.
   - `git clone https://github.com/{본인 github id}/KUIT5_Android_week1.git`
   - ex) `git clone https://github.com/ikseong00/KUIT5_Android_week1.git`
   - **Git Bash** 에는 **ctrl + v** 가 안되기 때문에 복사한 걸 붙여넣으려면 **shift + insert** 를 누르면 됩니다!

<img src="https://github.com/user-attachments/assets/d5025cf6-aebf-40c4-a719-192896af5fd8" width="480"/>

4. 그 후 엔터를 누르면 Git Clone 완료!

<img src="https://github.com/user-attachments/assets/9781a023-2817-4d65-9f67-2ce9e6e3cf16" width="480"/>

- 아래와 같이 디렉토리/폴더에 Github Repository 와 같은 이름의 폴더가 생기면 완료입니다.

<img src="https://github.com/user-attachments/assets/c73e08c9-1e5e-49ec-98d1-75acaff9b14a" width="480"/>

# 미션 제출 방법

## commit & push 후 Fork 저장소 접근

아래처럼 Compare & Pull Request 가 뜨면 버튼을 누르면 되고, 아니면

<img src="https://github.com/user-attachments/assets/03afad65-2de2-4b1b-b88a-0f775b75bcf4" width="600"/>

Contribute 버튼을 눌러서 Open pull request 를 누릅니다.

<img src="https://github.com/user-attachments/assets/566bd88d-65a1-49a1-9a3f-5685d555b66d" width="600"/>

**!! 주의사항 !!**
1. Open pull request 를 누르기 전에 Branch 가 설정한 브랜치가 맞는지 확인합니다.

<img src="https://github.com/user-attachments/assets/d5e58d00-e73a-4cc0-8247-a7eb6f403681" width="600"/>

2. 네모칸 쳐진 **base** 와 **compare** 가 본인이 설정한 브랜치가 맞는지 확인합니다. (가장 중요)
   - **base** 는 **원본 Repository** 의 브랜치이고, **compare** 는 **Fork 한 Repository** 의 브랜치입니다.
   - 꼭 자신의 브랜치에서 자신의 브랜치로만 pull request 요청을 보내주세요 !

<img src="https://github.com/user-attachments/assets/ec4a0d83-f8e7-4599-beb9-929f63db41a5" width="600"/>

## Pull Request 내용 작성하기

주의사항을 확인하셨다면, 미션 제출인 **Pull Request** 의 **title** 과 **description** 를 채워줍니다.

**title** 에는 다음과 같이 작성해주세요.
- N주차 미션 / N조 000
- ex) 1주차 미션 / 1조 조익성

**description** 에는 다음과 같은 양식으로 작성해주세요.

**미션**

> 각 주차에 해당하는 미션을 여기에다 작성하고, 완수했다면 체크표시합니다.

**구현에 대한 설명**

> 본인이 구현한 것에 대한 설명을 해주세요! (추후 프로젝트할 때 좋은 습관입니다.)

**스크린샷 & 실행영상**

> 실행영상이 있다면 실행영상을, 없다면 스크린샷을 첨부해주세요.

**기타**

> 기타 문의사항이나 질문사항, 그 외 하고싶은 말씀이 있다면 작성해주세요!

다 채우셨다면 하단에 Create pull request 를 눌러 완성하시면 됩니다 !

<img src="https://github.com/user-attachments/assets/c23e33f7-ef43-4fc2-8cd0-35b2cc601111" width="600"/>
