To use this application
- run the Main class 
```shell
./gradlew run -PmainClass=com.andy.p4_event_streams.p2_jukebox.Main
```
- start the jukebox controller with netcat
```shell
netcat localhost 3000
```
- schedule a song and then play
```shell
/schedule sample1.mp3
/play
```
