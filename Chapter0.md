# Remote Device에 있는 상태 정보를 Update하기 위한 기본 방법

# 자동화된 Update 구조의 필요성 #

> DLNA Device가 발견되면, CP에서는 발견한 Device의 종류 및 서비스를 자동적으로 알아야 할 필요가 있다. 마찬가지로, 사용자가 DLNA 기능을 이용하여, 내 PC에 있는 동영상 정보를 가져오거나, 활용할 경우, 동영상에 대한 상태 정보(동작 중, 멈춰 있는 중, 파일이 삭제되었음)를 자동적으로 알아와야 한다.

> 즉, 특정 시점(Event 발생 시점) 이후에는, 자동적으로 원격으로 접속하여, Control Point 내부 정보를 최신화 하는 일련의 Process가 필요하며, 이를 위하여, 공통적인 Routine을 정리하기로 한다.


# 세부 정보 #

자동적인 정보 Update가 필요한 항목들을 열거하면 다음과 같다.

1. SSDP 메시지에서 새로운 Device가 식별되는 경우,
> Device Description Document XML을 가지고 와서,
> 정확한 Device 정보를 모두 획득하여, 내부 Device정보를 갱신하는 경우
2. 사용자가 원격 Device에 있는 서비스를 사용하고자 하는 경우,
> 해당하는 Device에 GENA Subscribe를 요청하여, Subscrition Listening을
> 기동

유사하게, 자동적으로 Remote Device에 Notification을 하는 경우도 있다.

1. SSDP 에서 Timeout을 방지 하기 위하여, Update 메시지를 발송하는 경우
2. GENA Timeout을 방지 하기 위하여, Update 메시지를 발송하는 경우

# 공통 구조 #

Update가 필요한 정보의 경우, Observer pattern을 기본으로 활용한다.
모든 정보 관리는 Model Package에서 진행한다.

Updater는 Updater Package에서 진행한다.

1. Update Class instance 생성 시점
> - 모든 Updater Class는 공통의 Updater Interface를 상속하여,
> > Start/Stop을 외부에서 Control할 수 있도록 지원해야 하며,

> - Control Point에서 Start/Stop을 할 수 있도록 한다.
> - Updater Instance가 생성할 때, Mapping되는 Model에 자신을 등록하여,
> > Event 통지를 받을 수 있도록 준비를 하여야 한다.

2. Update 시점

> - 해당하는 모델에서 정보 변경이 발생하면, 이를 위에서 등록한 Listener를
> > 통하여, 전파하여야 한다.

> - 전파된 내용은 Updator Class Instace가 판단하여,
> - 외부에서 접속하고, 이를 갱신하여야 한다.
> - Model에서 전파되는 Transaction을 이용하지 말고, 독립적인 Thread를
> > 생성하여, Model 변경 시 지연 또는 멈춤 현상이 일어나게 하면 안된다.

# 실제 Interface #

**SSDP 에서 Device가 수정되는 경우를 위하여, 아래와 같은 작업을 추가**

1. UPnPDevice 변경 내용을 수신하기 위한 기본적인 Listener를 정의

> IUPnPDeviceListChangeListener
2. Updator Class를 생성함
> DeviceInfoUpdater implements IUPnPDeviceListChangeListener
3. Updator Class 생성자에서 자체 Thread Queue를 생성
4. Updator Class 변경 수신자에게는, Thread Queue에 Item을 넣는 역활을 제공
5. 이후, Updator Class에 있는 내부 Thread에서 동작

**사용자가 원격 Device에 있는 내용을 사용하는 경우,**

1. UPnPService 이벤트 변경 내용을 수신하기 위한 기본적인 Listener를 정의
> IUPnPServiceGenaListChangeListener
> (UPnPDevice의 경우, 등록이 되면, 무조건 갱신이 필요하지만,
> > UPnPService의 경우, GENA 등록이 되어 있지 않다면, 이후 Subscription 작업이
> > 필요하지 않다.)
2. Updator Class를 생성함
> > SubscribeServiceUpdator implements IUPnPServiceGenaListChangeListener
3. Updator Class 생성자에서 자체 Thread Queue를 생성
4. Updator Class 변경 수신자에게는 Thread Queue에 Item을 넣는 역활만 제공
5. 이후, Updator Class 내부 Thread에서 동작