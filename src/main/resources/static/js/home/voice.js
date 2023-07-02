window.SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

// 음성 인식 인스턴스 생성
const recognition = new SpeechRecognition();

// true면 음절을 연속적으로 인식하나 false면 한 음절만 기록함
recognition.interimResults = true;

// 값이 없으면 HTML의 <html lang="~~">을 참고함. 한국어 : ko-KR, 영어 : en-US
recognition.lang = "ko-KR";

// true means continuous, and false means not continuous (single result each time.)
// true면 음성 인식이 안 끝나고 계속 됨.
recognition.continuous = true;

// 숫자가 작으면 발음대로 적고, 크면 문장의 적합도에 따라 알맞은 단어로 대체함.
// 즉 maxAlternatives가 크면 이상한 단어도 문장에 적합하게 알아서 수정함.
recognition.maxAlternatives = 10000;

// 말을 시작하면 paragraph(class가 "para"인 p태그)를 만들고 말을 멈추면 paragraph를 끝냄.
let p = document.createElement("p");
p.classList.add("para");
let words = document.querySelector(".words");
words.appendChild(p);

let mic = document.querySelector("#circlein");
let speechToText = "";

recognition.addEventListener("result", (e) => {
  let interimTranscript = "";
  
  for (let i = e.resultIndex, len = e.results.length; i < len; i++) {
	
    let transcript = e.results[i][0].transcript;
    
    console.log(transcript);
    
    // 내가 말하는동안 들리는 어절들을 인식한뒤 조금씩 조합함
    // 조금씩 조합해서 마지막에는 완벽한 문장을 만들어냄 (rendering)
    // isFinal은 그 마지막 문장임 (말을 멈춰서 음성 인식이 끝남을 의미)
    if (e.results[i].isFinal) {
      speechToText += transcript;
    } else {
      interimTranscript += transcript;
    }
  }
  
  // 음성인식이 끝나면 마이크 배경 없애기
  recognition.addEventListener('soundend', () => {
    mic.style.backgroundColor = null;
  });
  
  document.querySelector(".para").innerHTML = speechToText + interimTranscript;
  
});

// 음성인식이 끝나면 자동으로 재시작한다.
// 이걸 막아두면 음성인식이 끝나면 마이크가 알아서 꺼진다.
// recognition.addEventListener("end", recognition.start);

// 마이크 버튼을 누르면 음성 인식 시작
mic.addEventListener("click", () => {
  recognition.start();
  mic.style.backgroundColor = "#6BD6E1"
})


// 필기 내용 복사하는 함수
function copyToClipBoard(){
var content = $('.para').html();

navigator.clipboard.writeText(content)
    .then(() => {
    alert("필기 내용이 복사되었습니다.");
})
    .catch(err => {
	alert('필기 내용 복사에 실패했습니다.');
    console.log('Something went wrong', err);
})

}
