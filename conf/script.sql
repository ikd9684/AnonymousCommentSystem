create table Comment(
  comment_id char(36) PRIMARY KEY,
  thread_id varchar(32) NOT NULL,
  timestamp char(23) NOT NULL,
  comment varchar(1024) NOT NULL
)

insert into Comment values('8ad11d42-8273-32ab-96be-ee36b2c7db98', 'default', '2015/05/03 01:23:41.001', 'これはテストメッセージです。');
insert into Comment values('6863b902-a716-375b-920f-23ee8ca7d0a5', 'default', '2015/05/03 01:37:05.102', 'ごにょごにょ...');
insert into Comment values('b8ca6179-7329-3174-ac36-720077a452b7', 'default', '2015/05/03 01:45:23.063', 'つれづれなるまゝに、日暮らし、硯にむかひて、心にうつりゆくよしなし事を、そこはかとなく書きつくれば、あやしうこそものぐるほしけれ。（Wikipediaより）');
insert into Comment values('dsar1342-8273-32ab-96be-ee36b2c7db98', 'default', '2015/05/02 12:23:41.005', 'これはテストテストメッセージです。');
insert into Comment values('34gfb902-a716-375b-920f-23ee8ca7d0a5', 'default', '2015/05/02 13:37:05.106', 'もごもごもごもご');
insert into Comment values('65gd6179-7329-3174-ac36-720077a452b7', 'default', '2015/05/02 14:45:23.067', '親譲りの無鉄砲で小供の時から損ばかりしている。（青空文庫より）');


