# ATM Controller 구현
## Git Clone 방법
아래의 명령어를 실행하여 로컬에 프로젝트를 클론합니다.
```
git clone https://github.com/symaeng98/ATM-Controller.git
```  

## 테스트 방법
테스트는 app/src/test/java/bank/atm 하위에 있으며 총 3개의 파일로 구성됩니다.
1. CardControllerTest.java: Card 엔티티 관련 테스트입니다.
2. AccountControllerTest.java: Account 엔티티 관련 테스트입니다.
3. **ATMScenarioTest.java**: 기본 시나리오 테스트입니다.

### 1번
```
./gradlew test --tests CardControllerTest
```  

### 2번
```
./gradlew test --tests AccountControllerTest
```  

### 3번 
프로그램의 플로우를 보여주기 위해 System.out.println으로 출력합니다.
이때 한 번 진행한 테스트는 up-to-date 처리되어 출력이 되지 않기 때문에 "--rerun-tasks" 옵션을 사용합니다.
```
./gradlew test --tests ATMScenarioTest --rerun-tasks
```

### 전체 테스트
한 번에 모든 테스트를 확인하고 싶다면 아래의 명령어를 사용합니다.
```
./gradlew test
```
