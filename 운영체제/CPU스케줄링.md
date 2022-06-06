# CPU 스케줄링

운영체제 내부에서 동작하는 여러 종류의 프로세스 중에서 누가 먼저 CPU 사용권을 가져야 하고 가지지 않아야 할까?

CPU 스케줄링은 누구(프로세스)에게 CPU를 주고, 얼마만큼의 시간만큼 줄 것인가에 대한 내용을 말한다.

프로세스는 그 특성에 따라 다음 두 가지로 나누어 진다.

- **IO-bound process**
    - CPU를 잡고 계산하는 시간보다 I/O에 많은 시간이 필요한 job.
    - (many short CPU bursts): 짧은, 하지만 많은 횟수의 CPU 점유.

- **CPU-bound process**
    - 계산 위주의 job
    - (few very long CPU bursts): 긴, 하지만 적은 횟수의 CPU 점유.

*CPU bursts는 한 번에 연속적으로 CPU를 점유해서 사용하는 시간을 나타낸다.*

## CPU Scheduler & Dispatcher

CPU Scheduler에 의해 사용될 프로세스를 선택하고, Dispatcher가 선택된 프로세스에게 CPU 사용권을 넘긴다.

- **CPU Scheduler**
    - Ready 상태의 프로세스 중에서 이번에 CPU를 줄 프로세스를 고른다.

- **Dispatcher**
    - CPU의 제어권을 CPU Scheduler에 의해 선택된 프로세스에게 넘긴다.
    - 이 과정을 문맥 교환(Context Switch)이라고 한다.

CPU 스케줄링이 필요한 경우는, 프로세스에게 다음과 같은 상태 변화가 있는 경우이다.

1. Running -> Blocked
    - ex) CPU를 사용하다가 I/O와 같은 작업 시간이 오래 걸리는 요청이 들어왔을 때, 해당 프로세스가 계속 CPU를 점유하고 있어봤자 소용없기 때문에 다른 프로세스에게 CPU 사용권이 넘어간다.

2. Running -> Ready
    - ex) 할당시간 만료로 timer interrupt

3. Blocked -> Ready
    - ex) I/O 완료 후 인터럽트(프로세스의 I/O가 완료되면 CPU를 사용할 수 있는 상황, Ready 상태가 되고, CPU 스케줄링을 통해 CPU 사용권을 얻을 수 있다)

4. Terminate(프로세스 종료)

**NOTE**

- 1번, 4번에서의 스케줄링은 nonpreemptive(강제로 빼앗지 않고 자진 반납)

- 나머지 경우의 스케줄링은 preemptive(강제로 빼앗음)

## Schedling Criteria(= Performance Measure, 성능 척도)

- CPU utilization(이용률)
   - keep the CPU as busy as possible

- Throughput(처리량)
    - number of processes that complete their execution per time
    - 시스템 입장에서 CPU가 얼마나 많은 일을 했느냐를 판단

- Turnaround time(소요시간, 반환시간)
    - amount of time to execute a particular process

- Waiting time(대기 시간)
    - amount of time a process has been waiting in the read y queue
    - CPU 사용을 위해 기다린 시간의 합

- Response time(응답 시간)
    - amount of time it takes from when a request was submitted until the first response is produced, not output
    - CPU 사용 요청 후 최초의 응답이 오기까지의 시간

**CPU 이용률이 높을수록, 처리량이 많을수록, 소요시간, 대기 시간, 응답 시간은 짧을수록 좋다.**

## CPU 스케줄링의 여러가지 알고리즘

## FCFS(FirstCome First-Served)

먼저 도착한 순서대로 처리.

프로세스 작업 시간과 상관 없이 무조건 도착 순서대로 처리하기 때문에 비효율적. 특히 긴 작업시간의 프로세스 뒤에 짧은 작업시간의 프로세스가 오는 경우 긴 작업시간의 프로세스 하나 때문에 전체 작업이 지체되는 현상이 발생하는데, 이를 Convoy Effect라고 한다.

## SJF(Shortest Job First)

- 각 프로세스의 다음번 CPU burst time(CPU 점유하여 작업하는 시간)을 가지고 스케줄링에 활용.

- CPU burst time이 가장 짧은 프로세스를 제일 먼저 스케줄.

주어진 프로세스들에 대해 minimum average waiting time을 보장하는 측면에서 SJF는 최적의 알고리즘이다.

SJF는 두 가지 종류로 나눌 수 있다.

- Nonpreemptive
    - 일단 CPU를 잡으면 이번 CPU burst가 완료될 때까지 CPU를 선점(preemptive) 당하지 않음.

- Preemptive
    - 현재 수행중인 프로세스의 남은 burst time보다 더 짧은 CPU burst time을 가지는 새로운 프로세스가 도착하면 CPU를 빼앗긴다.
    - 이 방법을 Shortest-Remaining-Time-First(SRTF)이라고도 부른다.

Preemptive한 방법이 minimum average waiting time 측면에서 가장 좋다.

SJF는 두 가지 약점이 있다.

- 기아 현상(Starvation)을 발생시킨다. 
    - 효율성만 강조하다 보니 짧은 프로세스가 무조건 우선권을 가지기 때문에 긴 작업시간의 프로세스는 영원히 기다려야만 할 수도 있다.

- 프로세스를 선택하는 시점에서 해당 프로세스의 작업 시간이 짧은지 어떤지 명확히 판단할 수 있는 방법이 없다.
    - 짧은 작업시간의 프로세스인 줄 알았는데 긴 작업시간의 프로세스인 경우가 있을 수 있다. 이는 구조적 문제 때문에 발생한다.
    - 그래서 SJF 알고리즘에서는 CPU burst time을 **추정(estimate)**하는 방법을 통해 프로세스를 스케줄링한다.

SJF의 CPU burst time 예측은,

- 과거의 CPU burst time을 이용해서 추정한다.

## Round Robin

- 각 프로세스는 동일한 크게의 할당  시간(time quantum)을 가진다.

- 할당 시간이 지나면 프로세스는 선점(preempted)당하고 ready queue의 제일 뒤에 가서 다시 줄을 선다.

- n개의 프로세스가 ready queue에 있고 할당 시간이 q time unit인 경우 각 프로세스는 최대 q time unit 단위로 1/n을 얻는다.
    - 어떤 프로세스도 (n-1)/q time unit 이상 기다리지 않는다.

일반적으로 SJF보다 average turnaround time이 길지만, response time은 더 짧다.

## 출처

- [운영체제](http://www.kocw.net/home/cview.do?cid=4b9cd4c7178db077)