# Twinkle

## 整体架构

### 软件系统架构:

​	Android+okhttp连接+Spring boot服务器+oracle数据库+腾讯云端

### 实体类：

​	Song

​	Songlist

​	News

​	NewsComment

​	User

​	Album

​	Singer

### 细节实现：

​	ViewPage+fragment实现顶部导航栏

​	PullToRefresh库实现下拉刷新页面



## 推荐音乐部分

### 界面展示:

![](doc/image/findmusic.png)



![](doc/image/日推.png)

### 主要涉及类：

​	FgFindmusicFragment

​	RecommendSongActivity

### 实现功能：

​	推荐单曲、推荐歌单



## 个人音乐部分

### 界面展示

![](doc/image/2.png)



![](doc/image/N@ZBC_V3U(`[BM}0HW{I[)9.png)

![](doc/image/添加歌单.png)

![](doc/image/删除歌单.png)

![](doc/image/歌单详情.png)

![](doc/image/3.png)

![](doc/image/4.png)

### 主要涉及类

​	FgMymusicFragment

​	Songlistadapter

​	ShareSonglistActivity

​	ShareSongActivity

​	ManageSonglistActivity

​	ManageSonglistAdapter

​	EditSonglistActivity

​	EditSonglistNameActivity

​	SonglistDetailActivity

 	SonglistDetailAdapter

​	AddSongAdapter

### 实现功能

​	查看自己的创建的和收藏歌单

​	新建歌单

​	修改歌单信息

​	删除歌单

​	从歌单中删除歌曲

​	把歌曲添加到我喜欢

​	把歌曲从我喜欢中删除

​	收藏歌单和取消收藏歌单

​	添加歌单到歌单

​	分享歌单

​	分享单曲



## 好友动态部分

### 界面展示

![](doc/image/@9KF23ZKU73E8Z~8GJX$_]W.png)

![](doc/image/75HF0TMW1{2`3P1@}@J4PCV.png)

![](doc/image/3E~`NA_T@V4DQEYH0DF7GQO.png)



![](doc/image/1.png)

### 主要涉及类

​	FgFriendsFragment

​	NewsDetailActivity

​	NewsAdapter

​	NewsCommentAdapter

​	friendsAdapter

​	ShareNewsActivity

### 实现功能

​	查看自己和好友的动态

​	删除自己的动态

​	取关好友

​	查看自己关注的人

​	给动态点赞和取消点赞

​	转发动态

​	查看动态详情

​	查看被转发的动态的详情

​	评论动态

​	回复评论

​	给评论点赞和取消点赞

​	播放动态中的歌曲

​	查看动态中的歌单



## 搜索部分

### 界面展示

![](doc/image/CR7EJI8`{U6RK_OJ1VUA4TV.png)

![](doc/image/Y[G5U}W5I}Z096BWFEFQMMF.png)

![](doc/image/AJD%TJZFGXDYO47EJI%ZOOA.png)

### 主要涉及类

​	FgsearchsongFragment

​	FgsearchsonglistFragment

​	FgsearchuserFragment

​	SearchMainAcitivity

​	SearchSongAdapter

​	SearchSonglistAdapter

​	SearchUserAdapter

### 实现功能

​	搜索歌曲

​	搜索歌单

​	搜索用户

​	播放歌曲

​	将歌曲加入下一首播放

​	分享歌曲

​	分析歌单

​	查看歌单

​	关注和取消关注用户

​	将歌曲加入歌单

​	播放列表部分





## 播放列表部分

### 界面展示:

![](doc/image/5.png)

### 主要涉及类：

​	PlayingSongList



### 实现功能：

​	独立于收藏歌单、云歌单的播放列表。是直接与用户和播放器以及云歌单进行交互的唯一类。

​	用户选中歌单后，在各单详情页面选中任意单独歌曲，都会先获得播放列表的唯一实例，并调用InitPlayingSongList() 函数，讲当前所选歌单的歌曲复刻至该播放列表内部数组中（Array List<Song>）中。同时会将定位指针（PointerIndex）指向当前选中的歌曲。随后，系统调用播放器相关函数，播放当前播放列表中定位指针所指向的歌曲。

​	添加/删除播放列表中指定歌曲，支持添加到末尾或下一首。	

​	支持清空播放列表。

​	可在播放器界面显示。





## 音乐播放器部分

### 界面展示

![](doc/image/6.png)



### 实现功能

​	歌曲的播放暂停

​	歌曲的标题与封面展示

​	超长标题的滚动展示

​	歌曲封面的旋转展示

​	歌曲播放进度的实时追踪

​	歌曲播放进度的实时调整

​	自由切歌（上/下一首 或 直接点选）

​	实现播放音乐Service化，支持退出界面后继续播放

​	退出后仍然保存进度，返回播放器界面后继续播放

​	跳转至分享界面

​	添加或移除收藏

​	展示当前的播放列表



### 主要涉及类

​	PlayerActivity

​	MusicService

​	PlayingSongList

​	ImageViewCircle

​	TextViewRoll

​	

​	



