# let, const 키워드

## var 키워드로 선언한 변수의 문제점

### 변수 중복 선언 허용
<pre>
<code>
var x = 1;
var y = 2;

// var 키워드로 선언된 변수는 같은 스코프 내에서 중복 선언을 허용한다.
// 초기화문이 있는 변수 선언문은 var 키워드가 없는 것처럼 동작한다.
var x = 7;

// 초기화문이 없는 변수 선언문은 무시된다.
var y;

console.log(x); // 7
console.log(y); // 2
</code>
</pre>

### 함수 레벨 스코프
함수 외부에서 var 키워드로 선언한 변수는 코드 블록 내에서 선언해도 모두 전역 변수가 된다.
<pre>
<code>
var i = 7;

for(int i = 0; i<=5; i++) {
    console.log(i);
}

console.log(i); // 5
</code>
</pre>

### 변수 호이스팅
var 키워드로 변수를 선언하면 변수 호이스팅에 의해 변수 선언문이 스코프의 선두로 끌어 올려진 것처럼 동작한다. 변수 호이스팅에 의해 var 키워드로 선언한 변수는 변수 선언문 이전에 참조할 수 있다. 단, 할당문 이전에 변수를 참조하면 언제나 undefined를 반환한다.

<pre>
<code>
// 이 시점에서 변수 호이스팅에 의해 이미 hello 변수가 선언되었다.(1. 선언단계)
// 변수 hello는 undefined로 초기화된다.(2. 초기화 단계)
console.log(hello); // undefined

// 변수에 값을 할당(3. 할당 단계)
hello = 'hello';

console.log(hello); // hello

// 변수 선언은 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 실행된다.
var foo;
</code>
</pre>
변수 선언문 이전에 변수를 참조하는 것은 변수 호이스팅에 의해 에러를 발생시키지는 않지만 프로그램의 흐름상 맞지 않다. 또한 가독성을 떨어뜨리고 오류를 발생시킬 여지를 남긴다.

## let 키워드
var 키워드의 단점을 보완하기 위해 ES6에서는 let, const 키워드를 도입했다. 

### 변수 중복 선언 금지
var 키워드는 변수 중복 선언을 허용했지만, let 키워드는 같은 이름의 변수를 중복 선언하면 문법 에러(Syntax Error)가 발생한다.
<pre>
<code>
// var 키워드로 선언한 변수는 같은 스코프 내에서 중복 선언을 허용한다.
var hello = 'hello';
var hello = 'hello there';

// let, const로 선언한 변수는 같은 스코프 내에서 중복 선언을 서용하지 않는다.
let welcome = 'welcome';
let welcome = 'welcome bro'; // SyntaxError: Identifier 'welcome' has already been declared
</code>
</pre>

### 블록 레벨 스코프
var 키워드로 선언한 변수는 오로지 함수의 코드 블록만을 지역 스코프로 인저아는 함수 레벨 스코프를 따른다. 하지만 let 키워드로 선언한 변수는 모든 코드 블록(함수, if문, for문, while문, try-catch문 등)을 지역 스코프로 인정하는 블록 레벨 스코프(block-level scope)를 따른다.
<pre>
<code>
let x = 1; // 전역 변수

{
    let x = 2; // 지역 변수
    let y = 3; // 지역 변수
}

console.log(x); // 1
console.log(y); // ReferenceError: y is not defined
</code>
</pre>

let 키워드로 선언된 변수는 블록 레벨 스코프를 따른다. 따라서 위 예제의 코드 블록 내에서 선언된 x, y 변수는 지역 변수다. 전역에서 선언된 x 변수와 코드 블록 내에서 선언된 x 변수는 다른 별개의 변수다. 또한 y 변수도 블록 레벨 스코프를 갖는 지역 변수다. 따라서 전역에서는 x 변수를 참조할 수 없다.

함수도 코드 블록이므로 스코프를 만든다. 함수 내의 코드 블록은 함수 레벨 스코프에 중첩된다.
<pre>
<code>
let i = 777;

function foo() {
    let i = 100;
    for(int i = 0; i<3; i++) {
        console.log(i); // 1 2
    }
    console.log(i); // 100
}

console.log(i); // 777
</code>
</pre>

### 변수 호이스팅
let 키워드로 선언한 변수는 변수 호이스팅이 발생하지 않는 것처럼 동작한다.

<pre>
<code>
console.log(i); // ReferenceError: Cannot access 'i' before initialization
let i;
</code>
</pre>
let 키워드로 선언한 변수는 선언문 이전에 해당 변수를 참조하면 에러가 발생한다.

var 키워드로 선언한 변수는 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 '선언 단계', '초기화 단계'가 한번에 진행된다. 즉 선언 단계에서 스코프(실행 컨텍스트의 렉시컬 환경_lexical environment)에 변수 식별자를 등록해 자바스크립트 엔진에 변수의 존재를 알린다. 그리고 즉시 초기화 단계에서 undefined로 변수를 초기화한다. 따라서 변수 선언문 이전에 변수에 접근해도 스코프에 변수가 존재하기 때문에 에러가 발생하지 않는다. 다만 undefined를 반환한다. 이후 변수 할당문에 도달하면 비로소 값이 할당된다.
<pre>
<code>
// var 키워드로 선언한 변수는 런타임 이전에 선언 단계, 초기화 단계가 실행된다.
// 따라서 변수 선언문 이전에 변수를 참조할 수 있다.
console.log(i); // undefined

var i;
console.log(i); // undefined

i = 1;
console.log(i); // 1
</code>
</pre>

**let 키워드로 선언한 변수는 '선언 단계', '초기화 단계'가 분리되어 진행된다.** 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 선언 단계가 먼저 실행되지만 초기화 단계는 변수 선언문에 도달했을 때 실행된다.

만약 초기화 단계가 실행되기 이전에 변수에 접근하려고 하면 에러가 발생한다. let 키워드로 선언한 변수는 스코프의 시작 지점부터 초기화 단계 시작 지점(변수 선언문)까지 변수를 참조할 수 없다. 스코프 시작 지점부터 초기화 시작 지점까지 변수를 참조할 수 없는 구간을 **일시적 사각지대(Temporal Dead Zone_TDZ)**라고 부른다.
<pre>
<code>
// 런타임 이전에 선언 단게가 실행된다. 아직 변수가 초기화되지 않았다.
// 초기화 이전의 일시적 사각지대에서는 변수를 참조할 수 없다.
console.log(i); // ReferenceError: Cannot access 'i' before initialization

let i; // 변수 선언문에서 초기화 단계가 실행된다.
console.log(i); // undefined

i = 1; 
console.log(i); // 1
</code>
</pre>

let 키워드로 선언한 변수는 변수 호이스팅이 발생하지 않는 것처럼 보이지만 그렇지 않다.
<pre>
<code>
let i = 777;

{
    console.log(i); // ReferenceError: Cannot access 'i' before initialization
    let i = 100; // local variable
}
</code>
</pre>
let 키워드로 선언한 변수는 변수 호이스팅이 발생하지 않는다면 전역변수 i를 참조해야 하지만 let 키워드로 선언한 변수도 호이스팅이 발생하기 때문에 에러가 발생했다.

### 전역 객체와 let
var 키워드로 선언한 전역 변수와 전역 함수, 그리고 선언하지 않은 변수에 값을 할당한 암묵적 전역은 전역 객체 window의 프로퍼티가 된다. 전역 객체의 프로퍼티를 참조할 때 window를 생략할 수 있다.
<pre>
<code>
// 전역 변수
var x = 1;

// 암묵적 전역
y = 2;

// 전역 함수
function foo() {}

// var 키워드로 선언한 전역 변수는 전역 객체 window의 프로퍼티다.
console.log(window.x); // 1

// 전역 객체 window의 프로퍼티는 전역 변수처럼 사용할 수 있다.
console.log(x); // 1

// 암묵적 전역은 전역 객체 window의 프로퍼티다.
console.log(window.y); // 2
console.log(y); // 2

// 함수 선언문으로 정의한 전역 함수는 전역 객체 window의 프로퍼티다.
console.log(window.foo); // ƒ foo() {}

// 전역 객체 window의 프로퍼티는 전역 변수처럼 사용할 수 있다.
console.log(foo); // ƒ foo() {}
</code>
</pre>

let 키워드로 선언한 전역 변수는 전역 객체의 프로퍼티가 아니다. 즉 window.foo와 같이 접근할 수 없다. let 전역 변수는 보이지 않는 개념적인 블록내에 존재하게 된다.
<pre>
<code>
let x = 1;

// let, const 키워드로 선언한 전역 변수는 전역 객체 window의 프로퍼티가 아니다.
console.log(window.x); // undefined
console.log(x); // 1
</code>
</pre>

## const 키워드
const 키워드는 상수(constant)를 선언하기 위해 사용한다. 하지만 반드시 상수만을 위해 사용하지는 않는다. const 키워드의 특징은 let 키워드와 대부분 동일하지만 약간 다른점이 있다.

### 선언과 초기화
**const 키워드로 선언한 변수는 반드시 선언과 동시에 초기화해야 한다.**

<pre>
<code>
// 이렇게 선언과 동시에 초기화해야 한다.
const i = 1;

// 초기화하지 않으면 에러 발생.
const i; // yntaxError: Missing initializer in const declaration
</code>
</pre>

const 키워드로 선언한 변수 또한 let 키워드로 선언한 변수와 마찬가지로 블록 레벨 스코프를 가지며, 변수 호이스팅이 발생하지 않는 것처럼 동작한다.
<pre>
<code>
{
    // 변수 호이스팅이 발생하지 않는 것처럼 동작.
    console.log(foo); // ReferenceError: Cannot access 'foo' before initialization
    const foo = 1;
    console.log(foo); // 1
}

// 블록 레벨 스코프를 갖는다.
console.log(foo); 
</code>
</pre>

### 재할당 금지
var, let 키워드로 선언한 변수는 재할당이 가능하지만, **const 키워드로 선언한 변수는 재할당이 금지된다.**
<pre>
<code>
const i = 1;
i = 2; // TypeError: Assignment to constant variable.
</code>
</pre>

### 상수
const 키워드로 선언한 변수에 원시 값을 할당한 경우 변수 값을 변경할 수 없다. 원시 값은 변경 불가능한 값(immutable value)이므로 재할당 없이 값을 변경할 수 있는 방법이 없다. 이런 특징을 이용해 const 키워드를 상수를 표현하는데 사용한다.

변수의 상대 개념인 상수는 **재할당이 금지된 변수**를 말한다. 상수도 값을 저장하기 위한 메모리 공간이 필요하므로 변수라고 할 수 있다. 단, 변수는 언제든지 재할당을 통해 변수 값을 변경할 수 있지만 상수는 재할당이 금지된다.

상수는 상태 유지와 가독성, 유지보수의 편의를 위해 적극적으로 사용해야 한다. 
<pre>
<code>
// price before Tax
let preTax = 100;

// price after Tax
// 0.1의 의미를 명확히 알기 어렵기 때문에 가독성이 좋지 않다.
let afterTax = preTax + (preTax * 0.1);

console.log(afterTax); // 110
</code>
</pre>
코드 내의 0.1은 어떤 의미로 사용했는지 알기 어렵기 때문에 가독성이 좋지 않다. 또한 세율을 의미하는 0.1은 쉽게 바뀌지 않는 값이며, 프로그램 전체에 고정된 값을 사용해야 한다. 이때 세율을 상수로 정의하면 값의 의미를 쉽게 파악할 수 있고 변경될 수 없는 고정값으로 사용할 수 있다.

const 키워드로 선언된 변수는 재할당이 금지된다. **const 키워드로 선언된 변수에 원시 값을 할당한 경우 원시 값은 변경할 수 없는 값이고 const키워드에 의해 재할당이 금지되므로 할당된 값을 변경할 수 있는 방법은 없다.** 또한 상수는 프로그램 전체에서 공통적으로 사용하므로 나중에 세율이 변경되면 상수만 변경하면 되기 때문에 유지보수성이 높아진다.

일반적으로 상수 이름은 대문자로 선언해 상수임을 명확히 나타낸다. 여러 단어로 이루어진 경우 언더스코어(_)로 구분하여 스네이크 케이스로 표현한다.
<pre>
<code>
// 세율을 의미하는 0.1은 상수로 사용될 값.
// 변수 이름을 대문자로 선언해 상수임을 명확히 나타낸다.
const TAX_RATE = 0.1;

// price before Tax
let preTax = 100;

// price after Tax
let afterTax = preTax + (preTax * TAX_RATE);

console.log(afterTax); // 110
</code>
</pre>

### const 키워드와 객체
const 키워드로 선언된 변수에 원시 값을 할당한 경우 값을 변경할 수 없다. 하지만 **const 키워드로 선언된 변수에 객체를 할당한 경우 값을 변경할 수 있다.** 변경 불가능한 값이 원시 값은 재할당 없이 변경할 수 있는 방법이 없지만 변경 가능한 값이 객체는 재할당 없이도 직접 변경이 가능하다.
<pre>
<code>
const bike = {
    name: 'Triumph';
};

// 객체는 변경 가능한 값이다. 따라서 재할당 없이 변경이 가능하다.
bike.name = 'Kawasaki';

console.log(bike); // {name: 'Kawasaki'}
</code>
</pre>
**const 키워드는 재할당을 금지할 뿐 '불변'을 의미하지는 않는다.** 다시 말해, 새로운 값을 재할당하는 것은 불가능하지만 프로퍼티 동적 생성, 삭제, 프로퍼티 값의 변경을 통해 객체를 변경하는 것은 가능하다. 이때 객체가 변경되더라도 변수에 할당된 참조 값은 변경되지 않는다.

## var,let,const 어느 키워드로 변수를 생성해야 할까
변수 선언에는 기본적으로 const를 사용하고 let은 재할당이 필요한 경우에 한정해 사용하는 것이 좋다.
const 키워드를 사용하면 의도치 않은 재할당을 방지하기 때문에 좀 더 안전하다.
var와 let, const 키워드는 다음과 같이 사용하는 것을 권장한다.

* ES6를 사용하면 var 키워드를 사용하지 않는다.
* 재할당이 필요한 경우에 한정해 let 키워드를 사용한다. 이때 변수의 스코프는 최대한 좁게 만든다.
* 변경이 발생하지 않고 읽기 전용으로 사용하는(재할당이 필요 없는 상수) 원시 값과 객체에는 const 키워드를 사용한다. const 키워드는 재할당을 금지하므로 var, let 키워드보다 안전하다.

변수를 선언하는 시점에는 재할당이 필요할지 어떨지 잘 모른다. 의외로 객체는 재할당하는 경우가 드물다. 따라서 변수를 선언할 때 일단 const 키워드를 사용하자. 반드시 재할당이 필요하다면 그때 const 키워드를 let 키워드로 변경해도 결코 늦지 않다. 물론 그 전에 반드시 재할당이 필요한지 한 번 더 생각해보자.

# 출처
* [모던 자바스크립트 Deep Dive](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9791158392239&orderClick=LEA&Kc=)