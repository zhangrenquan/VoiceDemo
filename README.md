# VoiceDemo

demo地址    https://github.com/zhangrenquan/VoiceDemo.git

采用了Android音频播放的两种方式
    SoundPool 和 MediaPlayer
    两者区别是   SoundPool需要优先初始化  将音频加载到内存中   播放时从内存中获取音频文件  不加载无法播放
               MediaPlayer不需要初始化  随时都可以进行播放
               由此可见 SoundPool 播放会比MediaPlayer 更快一些
    
    SoundUtil  对SoundPool的封装   由于SoundPool是提前加载、缓冲在进行播放  所以播放组合音频时会同时播放  
    为了解决这一问题  我们可以对相应的音频进行延迟   详情可见SoundUtil代码
    
    MediaPlayUtil 对MediaPlayer的封装 由于某些极短音频无法播放（几毫秒状态）  这是建议使用SoundPool方法播放
    
    对于音频播放是异步的  否则会阻塞主线程  导致程序卡顿  所以使用了线程池 ----> ExecutorService
    
    MediaPlayer的基本使用：
                    （1） 创建MediaPlayer实例
                        可以使用直接new的方式：
                        MediaPlayer mp = new MediaPlayer();
                        也可以使用create的方式，如：
                        //这时就不用调用setDataSource了 
                        MediaPlayer mp = MediaPlayer.create(this, R.raw.test);
                    （2）设置播放源
                        MediaPlayer要播放的文件主要包括3个来源：
                            a. 用户在应用中事先自带的resource资源
                            例如：MediaPlayer.create(this, R.raw.test);
                            b. 存储在SD卡或其他文件路径下的媒体文件或存放在assets目录下
                            例如：mp.setDataSource("/sdcard/test.mp3");
                            c. 网络上的媒体文件
                            例如：mp.setDataSource(" http://www.citynorth.cn/music/confucius.mp3");
                        MediaPlayer的setDataSource一共四个方法：
                            setDataSource (String path)
                            setDataSource (FileDescriptor fd)
                            setDataSource (Context context, Uri uri)
                            setDataSource (FileDescriptor fd, long offset, long length)
                    （3）控制播放器的几个方法：
                            serDataSource()	设置要播放的音频文件的位置
                            prepare()	在开始播放之前调用这个方法完成准备工作         同步---->create方法创建的，那么第一次启动播放前不需要再调用prepare()了，因为create方法里已经调用过了。
                            prepareAsync() 在开始播放之前调用这个方法完成准备工作    异步
                            start()	开始或继续播放音频
                            pause()	暂停播放音频
                            reset()	将MediaPlayer对象重置到刚刚创建的状态    播放器从Error状态中恢复过来，重新会到Idle状态
                            seekTo()	从指定的位置开始播放音频                 可以让播放器从指定的位置开始播放，需要注意的是该方法是个异步方法,也就是说该方法返回时并不意味着定位完成，尤其是播放的网络文件，真正定位完成时会触发OnSeekComplete.onSeekComplete()监听
                            stop()	停止播放音频，调用这个方法后的MediaPlayer对象无法在播放音频
                            release()	释放掉与MediaPlayer对象相关的资源       一旦确定不再使用播放器时应当尽早调用它释放资源
                            isPlaying()	判断当前MediaPlayer是否正在播放音频
                            getDuration()	获取载入的音频文件时长
                            setLooping(boolean looping)：设置是否循环播放。
                            getCurrentPosition()：获取当前流媒体的播放的位置，单位是毫秒
                            isLooping()：判断是否循环播放
                            setAudioStreamType(int streamtype)：设置播放流媒体类型
                            setNextMediaPlayer(MediaPlayer next)：设置当前流媒体播放完毕，下一个播放的MediaPlayer
                    
                    （4） 设置不同的监听器 监听不同的播放状态
                            例如：
                            setOnCompletionListener(MediaPlayer.OnCompletionListener listener)
                            setOnErrorListener(MediaPlayer.OnErrorListener listener)等
                           设置播放器时需要考虑到播放器可能出现的情况设置好监听和处理逻辑，以保持播放器的健壮性和稳定性
                           
    SoundPool简单使用：
                    （1）创建SoundPool实例
                            SoundPool mSoundPool = new SoundPool.Builder()
                                      	                        .setMaxStreams(16)//同时播放流的最大数量，当播放的流的数目大于此值，则会选择性停止优先级较低的流
                                      	                        .build();
                            构造器如下： 
                            SoundPool(int maxStreams, int streamType, int srcQuality)
                            参数maxStreams：指定支持多少个声音； 
                            参数streamType：指定声音类型： 
                            参数srcQuality：指定声音品质。
                    （2）加载音频  load()
                            SoundPool提供了如下4个load方法：
                            //从 resld 所对应的资源加载声音。
                            int load(Context context, int resld, int priority)
                            //加载 fd 所对应的文件的offset开始、长度为length的声音。
                            int load(FileDescriptor fd, long offset, long length, int priority)
                            //从afd 所对应的文件中加载声音。
                            int load(AssetFileDescriptor afd, int priority)
                            //从path 对应的文件去加载声音。
                            int load(String path, int priority)
                    （3）播放   play()
                            play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
                            参数soundID：指定播放哪个声音； 
                            参数leftVolume、rightVolume：指定左、右的音量： 
                            参数priority：指定播放声音的优先级，数值越大，优先级越高； 
                            参数loop：指定是否循环，0：不循环，-1：循环，其他值表示要重复播放的次数；
                            参数rate：指定播放的比率，数值可从0.5到2， 1为正常比率。
                    （4）释放资源     release()
     注意* SoundPool 必须、必须、必须  先加载load在播放play*****