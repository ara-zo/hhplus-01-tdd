`ReentrantLock`을 사용한 락 메커니즘을 도입하여 여러 요청이 동시에 들어올 때도 안정적으로 동작할 수 있도록 설계하였습니다.

## LockService 동작

`LockService`는 `ConcurrentHashMap`과 `ReentranLock`을 사용하여 각 사용자 ID별로 락을 생성하고, 요청이 들어왔을 때 해당 사용자에 대한 동시 작업을 제어 합니다.

- ConcurrentHashMap 사용
  각 사용자 ID에 대한 ReentrantLock 객체를 관리
  `computeIfAbsent`를 사용하여 해당 ID에 대한 락이 없으면 새롭게 생성하고 있으면 기존의 락을 재사용 합니다.
- ReentrantLock 사용
  `ReentrantLock(true)`로 공정성을 보장하는 락을 생성합니다. 여러 스레드가 동시에 락을 기다릴때 공정하게 대기열에 따라 락을 얻을 수 있도록 합니다.
- tryLock
  1분간 락을 시도, 만약 시간이 지나도 락을 얻지 못하면 예외를 던져 작업이 실패했다고 알려줍니다.
  성공적으로 락을 얻으면 다음 작업으로 넘어갑니다.
- finally
  작업이 끝난 후에는 `finally`에서 반드시 `unlock()`을 호출하여 락을 해제 합니다. 따라서 락을 사용한 뒤 별도로 `unlock()`을 호출하지 않아도 됩니다.

## 문제점

성능 이슈

`ReentrantLock`을 사용한 락 기반 동시성 제어는 높은 동시성 환경에서는 잠재적으로 성능에 영향을 줄 수 있습니다. 락을 사용할 경우, 여러 스레드가 동일한 리소스에 대해 기다려야 하기 때문에 성능 저하가 발생할 수 있습니다.

## 결론

현재 코드에서는 사용자별로 `ReentrantLock`을 이용해 동시성 제어를 하고 있습니다.

락이 잘 해제되는 구조와 충돌이 없는 처리가 보장되기 때문에 여러 요청이 들어오더라도 정상적으로 처리될 수 있을 것으로 보입니다.

다만, 성능이나 예외 처리 관련 요구사항에 따라 코드를 추가적으로 최적화하거나 개선할 필요가 있습니다.