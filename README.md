### 2017-05-29 성유경
* 처음에는 기본 KMA Server를 구축해서 어디서나 kma client 모듈만 있으면 형태소 분석 결과를 가져가서 원하는대로 분석해서 쓰도록 하려고 했으나, 소켓통신의 불안정함으로 server 프로그램은 사실상 폐기.
* 잠정 폐기 코드
  - com.wisenut.core.*
  - com.wisenut.server.*
* komoran은 본 프로그램의 jar가 설치되는 디렉토리와 같은 곳에  models-full 디렉토리를 설치하면 그 디렉토리에서 사전을 로드해 구동되도록 배치파일을 작성하기를 권함..
* 현재 내 PC에서는 e:\dev\komoran에 모듈과 사전과 배치 파일이 설치되어 있음. 
