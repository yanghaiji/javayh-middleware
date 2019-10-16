# Redis 

## 一、Redis数据结构

 - String
>string 是 redis 最基本的类型，你可以理解成与 Memcached 一模一样的类型，
>一个 key 对应一个 value。string 类型是二进制安全的。
>意思是 redis 的 string 可以包含任何数据。比如jpg图片或者序列化的对象。
 string 类型是 Redis 最基本的数据类型，string 类型的值最大能存储 512MB。
 
 - List
> Redis 列表是简单的字符串列表，按照插入顺序排序。
>你可以添加一个元素到列表的头部（左边）或者尾部（右边）。
 - Set 
>Redis 的 Set 是 string 类型的无序集合。
 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
 - Zset(sorted set：有序集合)
>Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
 不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
   
   zset的成员是唯一的,但分数(score)却可以重复。
 - Hash
>Redis hash 是一个键值(key=>value)对集合。
 Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。
## 二、持久化方式
持久化介绍选自：https://www.cnblogs.com/qdhxhz/p/9131299.html

2.1 Redis有两种持久化的方式：快照（RDB文件）和追加式文件（AOF文件）

>   （1）RDB持久化方式是在一个特定的间隔保存某个时间点的一个数据快照。（默认模式）
>
>   （2）以日志的形式来记录每个写操作，将Redis执行过的所有写指令记录下来(读操作不记录)，只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作 。
>
>    **注意**：Redis的持久化是可以禁用的，两种方式的持久化是可以同时存在的，但是当Redis重启时，AOF文件会被优先用于重建数据。
   
2.2 RDB
>1、是什么？
>
>在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的Snapshot快照，它恢复时是将快照文件直接读到内存里。
    Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。
    整个过程中，主进程是不进行任何IO操作的，这就确保了如果需要进行大规模数据的恢复时还能有极高的性能。
![full stack developer tutorial](../../doc/image/redisrdb.png)
>2.文件路径和名称
>
>默认Redis会把快照文件存储为当前目录下一个名为dump.rdb的文件。要修改文件的存储路径和名称，可以通过修改配置文件redis.conf实现：
>RDB文件名，默认为dump.rdb。
>dbfilename dump.rdb
> 文件存放的目录，AOF文件同样存放在此目录下。默认为当前工作目录。
>
> dir ./
>
>3.redis.conf如何配置
>
>保存点可以设置多个，Redis的配置文件就默认设置了3个保存点：
>
>以下配置表示的条件：
>
>服务器在900秒之内被修改了1次
>
>save 900 1
>
> 服务器在300秒之内被修改了10次
>
>save 300 10
>
>服务器在60秒之内被修改了10000次
>
>save 60 10000
>
>如果想禁用快照保存的功能，可以通过注释掉所有"save"配置达到，或者在最后一条"save"配置后添加如下的配置：
>save ""

>4、如何启动快照
>有四种方式：
>  （1）在配置文件中你配置保存点，而不是save ""，当达到要求会进行快照。
>
>  （2）SAVE命令：SAVE命令会使用同步的方式生成RDB快照文件，这意味着在这个过程中会阻塞所有其他客户端的请求。因此不建议在生产环境使用这个命令。
>
>  （3）BGSAVE命令：BGSAVE命令使用后台的方式保存RDB文件，调用此命令后，会立刻返回OK返回码。Redis会产生一个子进程进行处理并立刻恢复对客户端的服务。在客户端我们可以使用LASTSAVE命令查看操作是否成功。
>
>  （4）执行flushall命令，也会产生dump.rdb文件，但里面是空的，无意义。
>
>   注意：配置文件里禁用了快照生成功能不影响SAVE和BGSAVE命令的效果。
 
2.2、AOF
>1.AOF是什么？
>
> 
>以日志的形式来记录每个写操作，将Redis执行过的所有写指令记录下来(读操作不记录)，只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作 。
> 
>注意：Aof保存的是appendonly.aof文件
![full stack developer tutorial](../../doc/image/redisaof.png)
>2、AOF启动、修复、恢复
>（1）正常启动
>
>修改默认的appendonly no，改为yes（因为redis默认是RDB持久化，所以这里需要在redis.conf手动开启）
>
>（2）如果启动失败
>
>有可能是你的appendonly.aof有误（比如因为磁盘满了，命令只写了一半到日志文件里，我们也可以用redis-check-aof这个工具很简单的进行修复。）
>
>具体做法：
>
>    redis-check-aof --fix进行修复
>
>3、同步策略
>
    它有三种同步策略：appendfsync always(每修改同步), appendfsync everysec(每秒同步)， appendfsync no （不同步）
    (1)appendfsync always(每修改同步)： 同步持久化 每次发生数据变更会被立即记录到磁盘 性能较差但数据完整性比较好。
    (2)appendfsync everysec(每秒同步)：异步操作，每秒记录. 如果发生灾难，您可能会丢失1秒的数据。
    (3)appendfsync no （不同步）：从不同步。
>官方建议使用默认配置每秒同步，它既快速又安全。这个always策略在实践中非常缓慢， 没有办法做得fsync比现在更快。
>4、AOF重写
>
     （1）是什么？
          AOF采用文件追加方式，文件会越来越大为避免出现此种情况，新增了重写机制,当AOF文件的大小超过所设定的阈值时，Redis就会启动AOF文件的内容压缩，
    只保留可以恢复数据的最小指令集.
    （2）重写原理
           AOF文件持续增长而过大时，会fork出一条新进程来将文件重写(也是先写临时文件最后再rename)，遍历新进程的内存中数据，每条记录有一条的Set语句。重写aof文件的操作，并没有读取旧的aof文件，
    而是将整个内存中的数据库内容用命令的方式重写了一个新的aof文件，这点和快照有点类似。
    （3）触发机制
           Redis会记录上次重写时的AOF大小，默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时触发（redis默认是64M进行重新一次，而实际生产环境这里至少要2G）
 
2.3、RDB和AOF优缺点
>1、RDB的优点
>
    （1）比起AOF，在数据量比较大的情况下，RDB的启动速度更快。
    （2）RDB文件是一个很简洁的单文件，它保存了某个时间点的Redis数据，很适合用于做备份。
    （3）RDB很适合用于灾备。单文件很方便就能传输到远程的服务器上。
    （4）RDB的性能很好，需要进行持久化时，主进程会fork一个子进程出来，然后把持久化的工作交给子进程，自己不会有相关的I/O操作。
>2、RDB缺点
>
    （1）RDB容易造成数据的丢失。假设每5分钟保存一次快照，如果Redis因为某些原因不能正常工作，那么从上次产生快照到Redis出现问题这段时间的数据就会丢失了。
    （2）RDB使用fork()产生子进程进行数据的持久化，如果数据比较大的话可能就会花费点时间，造成Redis停止服务几毫秒。
>3、AOF优点
>
    （1）该机制可以带来更高的数据安全性，即数据持久性。Redis中提供了3中同步策略，即每秒同步、每修改同步和不同步。事实上，每秒同步也是异步完成的，其效率也是非常高的，如果发生灾难，您只可能会丢失1秒的数据。
    （2）AOF日志文件是一个纯追加的文件。就算服务器突然Crash，也不会出现日志的定位或者损坏问题。甚至如果因为某些原因（例如磁盘满了）命令只写了一半到日志文件里，我们也可以用redis-check-aof这个工具很简单的进行修复。
    （3）当AOF文件太大时，Redis会自动在后台进行重写。重写很安全，因为重写是在一个新的文件上进行，同时Redis会继续往旧的文件追加数据。
>4、AOF缺点

    （1）在相同的数据集下，AOF文件的大小一般会比RDB文件大。
    （2）在某些fsync策略下，AOF的速度会比RDB慢。通常fsync设置为每秒一次就能获得比较高的性能，而在禁止fsync的情况下速度可以达到RDB的水平。
 
2.4、RDB和AOF建议

    （1）官方建议：是同时开启两种持久化策略。因为有时需要RDB快照是进行数据库备份，更快重启以及发生AOF引擎错误的解决办法。（换句话就是通过RDB来多备份一份数据总是好的）
    （2） 因为RDB文件只用作后备用途，建议只在Slave上持久化RDB文件，而且只要15分钟备份一次就够了，只保留save 900 1这条规则。
    （3）如果选择AOF，只要硬盘许可，应该尽量减少AOF rewrite的频率。因为一是带来了持续的IO，二是AOF rewrite的最后将rewrite过程中产生的新数据写到新文件造成的阻塞几乎是不可避免的。
        AOF重写的基础大小默认值64M太小了，可以设到5G以上。

## 三、配置文件
<hr><h4>参数说明</h4>
<p>redis.conf 配置项说明如下：</p>
<table class="reference">
<tr><th>序号</th><th>配置项</th><th>说明</th></tr>
<tr><td>
1</td>
<td><pre>daemonize no</pre></td>
<td>Redis 默认不是以守护进程的方式运行，可以通过该配置项修改，使用 yes 启用守护进程（Windows 不支持守护线程的配置为 no ）
</td></tr>
    
<tr><td>
2</td> 
<td><pre>pidfile /var/run/redis.pid</pre></td> 
<td>
当 Redis 以守护进程方式运行时，Redis 默认会把 pid 写入 /var/run/redis.pid 文件，可以通过 pidfile 指定

</td></tr>
    
<tr><td>

3</td> 
<td>
<pre>port 6379</pre>
</td> 
<td>
指定 Redis 监听端口，默认端口为 6379，作者在自己的一篇博文中解释了为什么选用 6379 作为默认端口，因为 6379 在手机按键上 MERZ 对应的号码，而 MERZ 取自意大利歌女 Alessia Merz 的名字

 </td></tr>
    
<tr><td>

4</td> 
<td><pre>bind 127.0.0.1</pre>
</td> 
<td>绑定的主机地址
 </td></tr>
    
<tr><td>
    

5</td> 
<td> <pre>timeout 300</pre></td> 
<td>
当客户端闲置多长时间后关闭连接，如果指定为 0，表示关闭该功能
 </td></tr>
    
<tr><td>
    
   

6</td> 
<td> <pre>loglevel notice</pre></td> 
<td>
指定日志记录级别，Redis 总共支持四个级别：debug、verbose、notice、warning，默认为 notice
 </td></tr>
    
<tr><td>
    

7</td> 
<td><pre>logfile stdout</pre></td> 
<td>日志记录方式，默认为标准输出，如果配置 Redis 为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给 /dev/null
 </td></tr>
    
<tr><td>
    

8</td> 
<td> <pre>databases 16</pre></td> 
<td>设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id
 </td></tr>
    
<tr><td>
    
    

9</td> 
<td>
<pre>save &lt;seconds&gt; &lt;changes&gt;</pre>

<p>Redis 默认配置文件中提供了三个条件：</p>

<p><strong>save 900 1</strong></p>
<p><strong>save 300 10</strong></p>
<p><strong>save 60 10000</strong></p>

<p>分别表示 900 秒（15 分钟）内有 1 个更改，300 秒（5 分钟）内有 10 个更改以及 60 秒内有 10000 个更改。</p>
</td> 
<td>

指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合
 </td></tr>
    
<tr><td>
    
 

10</td> 
<td><pre>rdbcompression yes</pre></td> 
<td>
指定存储至本地数据库时是否压缩数据，默认为 yes，Redis 采用 LZF 压缩，如果为了节省 CPU 时间，可以关闭该选项，但会导致数据库文件变的巨大
 </td></tr>
    
<tr><td>
    
    

11</td> 
<td> <pre>dbfilename dump.rdb</pre></td> 
<td>指定本地数据库文件名，默认值为 dump.rdb
 </td></tr>
    
<tr><td>
    

12</td> 
<td> <pre>dir ./</pre></td> 
<td>指定本地数据库存放目录
 </td></tr>
    
<tr><td>
    

13</td> 
<td> <pre>slaveof &lt;masterip&gt; &lt;masterport&gt;</pre> </td> 
<td>设置当本机为 slav 服务时，设置 master 服务的 IP 地址及端口，在 Redis 启动时，它会自动从 master 进行数据同步
</td></tr>
    
<tr><td>
    
    

14</td> 
<td>  <pre>masterauth &lt;master-password&gt;</pre></td> 
<td>当 master 服务设置了密码保护时，slav 服务连接 master 的密码

    
</td></tr>
    
<tr><td>
    
15</td> 
<td> <pre>requirepass foobared</pre></td> 
<td>设置 Redis 连接密码，如果配置了连接密码，客户端在连接 Redis 时需要通过 AUTH &lt;password&gt; 命令提供密码，默认关闭

 </td></tr>
    
<tr><td>
    

16</td> 
<td> <pre> maxclients 128</pre></td> 
<td>设置同一时间最大客户端连接数，默认无限制，Redis 可以同时打开的客户端连接数为 Redis 进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis 会关闭新的连接并向客户端返回 max number of clients reached 错误信息
</td></tr>
    
<tr><td>
    
   

17</td> 
<td> <pre>maxmemory &lt;bytes&gt;</pre></td> 
<td>指定 Redis 最大内存限制，Redis 在启动时会把数据加载到内存中，达到最大内存后，Redis 会先尝试清除已到期或即将到期的 Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis 新的 vm 机制，会把 Key 存放内存，Value 会存放在 swap 区
</td></tr>
    
<tr><td>
    

18</td> 
<td><pre>appendonly no</pre></td> 
<td>
指定是否在每次更新操作后进行日志记录，Redis 在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为  redis 本身同步数据文件是按上面 save 条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为 no
</td></tr>
    
<tr><td>
    
19</td> 
<td><pre>appendfilename appendonly.aof</pre></td> 
<td>指定更新日志文件名，默认为 appendonly.aof
</td></tr>
    
<tr><td>
     

20</td> 
<td><pre>appendfsync everysec</pre></td> 
<td>
<p>指定更新日志条件，共有 3 个可选值： </p>
<ul><li>
<strong>no</strong>：表示等操作系统进行数据缓存同步到磁盘（快） </li><li>
<strong>always</strong>：表示每次更新操作后手动调用 fsync() 将数据写到磁盘（慢，安全） </li><li>
<strong>everysec</strong>：表示每秒同步一次（折中，默认值）</li></ul>
</td></tr>
    
<tr><td>
    

 

21</td> 
<td><pre>vm-enabled no</pre></td> 
<td>
指定是否启用虚拟内存机制，默认值为 no，简单的介绍一下，VM 机制将数据分页存放，由 Redis 将访问量较少的页即冷数据 swap 到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析 Redis 的 VM 机制）
</td></tr>
    
<tr><td>
     
22</td> 
<td><pre>vm-swap-file /tmp/redis.swap</pre></td> 
<td>
虚拟内存文件路径，默认值为 /tmp/redis.swap，不可多个 Redis 实例共享
</td></tr>
    
<tr><td>
     
23</td> 
<td> <pre>vm-max-memory 0</pre></td> 
<td>将所有大于 vm-max-memory 的数据存入虚拟内存，无论 vm-max-memory 设置多小，所有索引数据都是内存存储的(Redis 的索引数据 就是 keys)，也就是说，当 vm-max-memory 设置为 0 的时候，其实是所有 value 都存在于磁盘。默认值为 0
</td></tr>
    
<tr><td>

24</td> 
<td><pre>vm-page-size 32</pre> </td> 
<td>Redis swap 文件分成了很多的 page，一个对象可以保存在多个 page 上面，但一个 page 上不能被多个对象共享，vm-page-size 是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page 大小最好设置为 32 或者 64bytes；如果存储很大大对象，则可以使用更大的 page，如果不确定，就使用默认值
</td></tr>
    
<tr><td>
     

25</td> 
<td><pre>vm-pages 134217728</pre></td> 
<td>设置 swap 文件中的 page 数量，由于页表（一种表示页面空闲或使用的 bitmap）是在放在内存中的，，在磁盘上每 8 个 pages 将消耗 1byte 的内存。
</td></tr>
    
<tr><td>
     
     

26</td> 
<td><pre>vm-max-threads 4</pre>
</td> 
<td>
设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
</td></tr>
    
<tr><td>
     

27</td> 
<td><pre>glueoutputbuf yes</pre>
</td> 
<td>

 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
</td></tr>
    
<tr><td>
    

28</td> 
<td>
<pre>hash-max-zipmap-entries 64
hash-max-zipmap-value 512</pre>
</td> 
<td>指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
</td></tr>
    
<tr><td>
    

29</td> 
<td><pre>activerehashing yes</pre>
</td> 
<td>
指定是否激活重置哈希，默认为开启（后面在介绍 Redis 的哈希算法时具体介绍）
</td></tr>
    
<tr><td>
    

30</td> 
<td>
<pre>include /path/to/local.conf</pre>
</td> 
<td>
指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件</td></tr></table>

