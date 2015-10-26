# GeoHash

1. 坐标转换成52bit二进制编码值。

    * 52bit精度在0.6m足够满足搜索范围需求。
    * 算法，根据经纬度计算GeoHash二进制编码：
    
        * 首先将纬度范围(-90, 90)平分成两个区间(-90, 0)、(0, 90)， 如果目标纬度位于前一个区间，则编码为0，否则编码为1。由于39.92324属于(0, 90)，所以取编码为1。然后再将(0, 90)分成 (0, 45), (45, 90)两个区间，而39.92324位于(0, 45)，所以编码为0。以此类推，直到精度符合要求为止，得到纬度编码为1011 1000 1100 0111 1001。
        * 经度也用同样的算法，对(-180, 180)依次细分，得到116.3906的编码为1101 0010 1100 0100 0100。
        * 接下来将经度和纬度的编码合并，奇数位是纬度，偶数位是经度，得到编码 11100 11101 00100 01111 00000 01101 01011 00001。 

2. 估算搜索范围起始值。

    * 算法, 如果用52位来表示一个坐标, 那么总共有: 2^26 * 2^26 = 2^52 个框:
    
        * 地球半径：radius = 6372797.560856m
        * 每个框的夹角：angle = 1 / 2^26 (2的26次方)
        * 每个框在地球表面的长度: length = 2 * π * radius * angle
        * 52bit:0.59m, 50bit:1.19m......,30bit:1221.97m, 28bit:2443.94m, 26bit:4887.87m, 24bit: 9775.75m......

3. 给出查询的中心坐标并计算其GeoHash值(52bit)。

4. 计算中心坐标相邻的8个坐标(中心坐标在两个框边界会有误差，此规避误差)。

5. 加上中心坐标共9个52bit的坐标值，针对每个坐标值参照搜索范围值算出区域值[MIN, MAX]。
    * 算法：MIN为坐标的搜索指定位起始长度后补零；MAX为坐标的搜索指定位终止长度后+1再补零。
    
6. 使用Redis命令ZRANGEBYSCORE key MIN MAX WITHSCORES查找。

7. 避免误差按照距离公式在将所有结果过滤一次(GeoHash反坐标再计算距离)。   