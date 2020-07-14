package com.jxf.svc.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;



public class RedisUtils {


    @SuppressWarnings("unchecked")
	private static RedisTemplate<Object,Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);

    public RedisTemplate<Object,Object> getInstance(){  
        return redisTemplate;  
    }
  
    
   /**
     * 设置 String 类型 key-value 
     * @param key 
     * @param value 
     */
    public static void set(String key,String value){  
      redisTemplate.opsForValue().set(key, value);  
    }

    
    /** 
     * 获取 String 类型 key-value 
     * @param key 
     * @return 
     */
    public static String get(String key){
        return (String)redisTemplate.opsForValue().get(key);  
    }

    /** 
     * 判断指定的key是否存在
     * @param key 
     * @return 
     */  
    public static Boolean hasKey(String key){  
        return redisTemplate.hasKey(key);  
    }  
    
    /** 
     * 设置 String 类型 key-value 并添加过期时间 (毫秒单位) 
     * @param key 
     * @param value 
     * @param time 过期时间,毫秒单位 
     */  
    public static void setForTimeMS(String key,String value,long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);  
    }

    /** 
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位) 
     * @param key 
     * @param value 
     * @param time 过期时间,分钟单位 
     */  
    public static void setForTimeMIN(String key,String value,long time){  
     redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);  
    }  
      
      
    /** 
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位) 
     * @param key 
     * @param value 
     * @param time 过期时间,分钟单位 
     */  
    public static void setForTimeCustom(String key,String value,long time,TimeUnit type){  
        redisTemplate.opsForValue().set(key, value, time, type);  
    }  
      
    /** 
     * 如果 key 存在则覆盖,并返回旧值. 
     * 如果不存在,返回null 并添加 
     * @param key 
     * @param value 
     * @return 
     */  
    public static String getAndSet(String key,String value){  
        return (String) redisTemplate.opsForValue().getAndSet(key, value);  
    }  
      
      
    /** 
     * 批量添加 key-value (重复的键会覆盖) 
     * @param keyAndValue 
     */  
    public static void batchSet(Map<String,String> keyAndValue){  
        redisTemplate.opsForValue().multiSet(keyAndValue);  
    }  
      
    /** 
     * 批量添加 key-value 只有在键不存在时,才添加 
     * map 中只要有一个key存在,则全部不添加 
     * @param keyAndValue 
     */  
    public static void batchSetIfAbsent(Map<String,String> keyAndValue){  
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);  
    }  
      
    /** 
     * 对一个 key-value 的值进行加减操作, 
     * 如果该 key 不存在 将创建一个key 并赋值该 number 
     * 如果 key 存在,但 value 不是长整型 ,将报错 
     * @param key 
     * @param number 
     */  
    public static Long increment(String key,long number){  
        return redisTemplate.opsForValue().increment(key, number);  
    }  
      
    /** 
     * 对一个 key-value 的值进行加减操作, 
     * 如果该 key 不存在 将创建一个key 并赋值该 number 
     * 如果 key 存在,但 value 不是 纯数字 ,将报错 
     * @param key 
     * @param number 
     */  
    public static Double increment(String key,double number){  
        return redisTemplate.opsForValue().increment(key, number);  
    }  
      
      
    /** 
     * 给一个指定的 key 值附加过期时间 
     * @param key 
     * @param time 
     * @param type 
     * @return 
     */  
    public static boolean expire(String key,long time,TimeUnit type){
    return redisTemplate.boundValueOps(key).expire(time, type);  
    }

    /** 
     * 移除指定key 的过期时间 
     * @param key 
     * @return 
     */  
    public static boolean persist(String key){  
        return redisTemplate.boundValueOps(key).persist();  
    }  
      
      
    /** 
     * 获取指定key 的过期时间 
     * @param key 
     * @return 
     */  
    public static Long getExpire(String key){  
        return redisTemplate.boundValueOps(key).getExpire();  
    }  
      
    /** 
     * 修改 key 
     * @param key 
     * @return 
     */  
    public static void rename(String key,String newKey){  
        redisTemplate.boundValueOps(key).rename(newKey);  
    }  
      
    /** 
     * 删除 key-value 
     * @param key 
     * @return 
     */  
    public static boolean delete(String key){
        return redisTemplate.delete(key);  
    }  
      
    //hash操作  
      
    /** 
     * 添加 Hash 键值对 
     * @param key 
     * @param hashKey 
     * @param value 
     */  
    public static void put(String key, String hashKey, String value){  
        redisTemplate.opsForHash().put(key, hashKey, value);  
    }  
    /** 
     * 添加 Hash 键值对 
     * @param key 
     * @param hashKey 
     * @param value 
     */  
    public static void put(Long key, String hashKey, String value){  
        redisTemplate.opsForHash().put(String.valueOf(key), hashKey, value);  
    }  
    /** 
     * 添加 Hash 键值对 
     * @param key 
     * @param hashKey 
     * @param value 
     */  
    public static void put(String key, String hashKey, int value){  
        redisTemplate.opsForHash().put(String.valueOf(key), hashKey, value);  
    }   
    /** 
     * 批量添加 hash 的 键值对 
     * 有则覆盖,没有则添加 
     * @param key 
     * @param map 
     */  
    public static void putAll(String key,Map<?,?> map){  
        redisTemplate.opsForHash().putAll(key, map);  
    }  
    /** 
     * 批量添加 hash 的 键值对 
     * 有则覆盖,没有则添加 
     * @param key 
     * @param map 
     */  
    public static void putAll(Long key,Map<?,?> map){  
        redisTemplate.opsForHash().putAll(String.valueOf(key), map);  
    }  
   
    /** 
     * 添加 hash 键值对. 不存在的时候才添加 
     * @param key 
     * @param hashKey 
     * @param value 
     * @return 
     */  
    public static boolean putIfAbsent(String key, String hashKey, String value){  
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);  
    }  
      
      
    /** 
     * 删除指定 hash 的 HashKey 
     * @param key 
     * @param hashKeys 
     * @return 删除成功的 数量 
     */  
    public static Long delete(String key, String ...hashKeys){  
        return redisTemplate.opsForHash().delete(key, hashKeys);  
    }  
      
      
    /** 
     * 给指定 hash 的 hashkey 做增减操作 
     * @param key 
     * @param hashKey 
     * @param number 
     * @return 
     */  
    public static Long increment(String key, String hashKey,long number){  
        return redisTemplate.opsForHash().increment(key, hashKey, number);  
    }  
      
    /** 
     * 给指定 hash 的 hashkey 做增减操作 
     * @param key 
     * @param hashKey 
     * @param number 
     * @return 
     */  
    public static Double increment(String key, String hashKey,Double number){  
        return redisTemplate.opsForHash().increment(key, hashKey, number);  
    }  
      
    /** 
     * 获取Hash 指定 Hashkey 下的 value 
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public static Object getHashKey(String key,String hashKey){  
        return redisTemplate.opsForHash().get(key, hashKey);  
    }  
    
    /** 
     * 获取Hash 指定 Hashkey 下的 value 
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public static Object getHashKey(Long key,String hashKey){  
        return redisTemplate.opsForHash().get(String.valueOf(key), hashKey);  
    }
      
    /** 
     * 获取Hash 所有 Hashkey 和 value 
     * @param key 
     * @return 
     */  
    public static Map<String,Object> getHashEntries(String key){  
    	
    	Map<Object,Object>  map1 = redisTemplate.opsForHash().entries(key);
    	Map<String,Object> map2 = new HashMap<String,Object>();
    	for(Map.Entry<Object,Object> entry :map1.entrySet()) {
    		map2.put((String)entry.getKey(), entry.getValue());
    	}   	
        return map2;  
    }  

    /** 
     * 获取Hash 所有 key 和 value 
     * @param key 
     * @return 
     */  
    public static Map<Object,Object> getHashEntries(Long key){  
        return redisTemplate.opsForHash().entries(String.valueOf(key));  
    }
      
    /** 
     * 验证指定 Hash 下 有没有指定的 hashkey 
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public boolean hashKey(String key,String hashKey){  
        return redisTemplate.opsForHash().hasKey(key, hashKey);  
    }  
      
    /** 
     * 获取 Hash 下的 所有 hashkey 字段名 
     * @param key 
     * @return 
     */  
    public static Set<Object> hashKeys(String key){  
        return redisTemplate.opsForHash().keys(key);  
    }  
      
      
    /** 
     * 获取指定 hash 下面的 键值对 数量 
     * @param key 
     * @return 
     */  
    public static Long hashSize(String key){  
        return redisTemplate.opsForHash().size(key);  
    }  
      
    //List 操作  
      
    /** 
     * 指定 list 从左入栈 
     * @param key 
     * @return 当前队列的长度 
     */  
    public static Long leftPush(String key,Object value){  
        return redisTemplate.opsForList().leftPush(key, value);  
    }  
      
    /** 
     * 指定 list 从左出栈 
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止 
     * @param key 
     * @return 出栈的值 
     */  
    public static Object leftPop(String key){  
        return redisTemplate.opsForList().leftPop(key);  
    }  
      
    /** 
     * 从左边依次入栈 
     * 导入顺序按照 Collection 顺序 
     * 如: a b c => c b a 
     * @param key 
     * @param values 
     * @return 
     */  
    public static Long leftPushAll(String key,Collection<Object> values){  
        return redisTemplate.opsForList().leftPushAll(key, values);  
    }  
      
    /** 
     * 指定 list 从右入栈 
     * @param key 
     * @return 当前队列的长度 
     */  
    public static Long rightPush(String key,Object value){  
        return redisTemplate.opsForList().rightPush(key, value);  
    }  
      
    /** 
     * 指定 list 从右出栈 
     * 如果列表没有元素,则返回空
     * @param key 
     * @return 出栈的值 
     */  
    public static Object rightPop(String key){  
    	if(hasKey(key)) {
    		return redisTemplate.opsForList().rightPop(key);
    	}
        return null;  
    }  
    
    /** 
     * 指定 list 从右出栈 
     * 如果列表没有元素, 会堵塞到列表一直有元素或者超时为止 
     * @param key 
     * @return 出栈的值 
     */  
    public static Object rightPop(String key,long timeout, TimeUnit unit){  
        return redisTemplate.opsForList().rightPop(key, timeout, unit);  
    } 
      
    /** 
     * 从右边依次入栈 
     * 导入顺序按照 Collection 顺序 
     * 如: a b c => a b c 
     * @param key 
     * @param values 
     * @return 
     */  
    public static Long rightPushAll(String key,Collection<Object> values){  
        return redisTemplate.opsForList().rightPushAll(key, values);  
    }  
      
      
    /** 
     * 根据下标获取值 
     * @param key 
     * @param index 
     * @return 
     */  
    public static Object popIndex(String key,long index){  
        return redisTemplate.opsForList().index(key, index);  
    }  
      
      
    /** 
     * 获取列表指定长度 
     * @param key 
     * @param index 
     * @return 
     */  
    public static Long listSize(String key,long index){  
        return redisTemplate.opsForList().size(key);  
    }  
      
  
    /** 
     * 获取列表 指定范围内的所有值 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public static List<Object> listRange(String key,long start,long end){  
        return redisTemplate.opsForList().range(key, start, end);  
    }  
      
  
    /** 
     * 删除 key 中 值为 value 的 count 个数. 
     * @param key 
     * @param count 
     * @param value 
     * @return 成功删除的个数 
     */  
    public static Long listRemove(String key,long count,Object value){  
        return redisTemplate.opsForList().remove(key, count, value);  
    }  
      
      
    /** 
     * 删除 列表 [start,end] 以外的所有元素 
     * @param key 
     * @param start 
     * @param end 
     */  
    public static void listTrim(String key,long start,long end){  
        redisTemplate.opsForList().trim(key, start, end);  
          
    }  
      
    /** 
     * 将 key 右出栈,并左入栈到 key2 
     * 
     * @param key 右出栈的列表 
     * @param key2 左入栈的列表 
     * @return 操作的值 
     */  
    public static Object rightPopAndLeftPush(String key,String key2){  
        return redisTemplate.opsForList().rightPopAndLeftPush(key, key2);  
          
    }  
      
    //set 操作  无序不重复集合  
      
    /** 
     * 添加 set 元素 
     * @param key 
     * @param values 
     * @return 
     */  
    public static Long add(String key ,String ...values){  
        return redisTemplate.opsForSet().add(key, values);  
    }  
      
    /** 
     * 获取两个集合的差集 
     * @param key 
     * @param key2 
     * @return 
     */  
    public static Set<Object> difference(String key ,String otherkey){  
        return redisTemplate.opsForSet().difference(key, otherkey);  
    }  
      
      
    /** 
     * 获取 key 和 集合  collections 中的 key 集合的差集 
     * @param key 
     * @param collections 
     * @return 
     */  
    public static Set<Object> difference(String key ,Collection<Object> otherKeys){  
        return redisTemplate.opsForSet().difference(key, otherKeys);  
    }  
      
    /** 
     * 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中 
     * @param key 
     * @param otherkey 
     * @param newKey 
     * @return 返回差集的数量 
     */  
    public static Long differenceAndStore(String key ,String otherkey,String newKey){  
        return redisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);  
    }  
      
    /** 
     * 将 key 和 集合  collections 中的 key 集合的差集 添加到  newkey 集合中 
     * @param key 
     * @param otherKeys 
     * @param newKey 
     * @return 返回差集的数量 
     */  
    public static Long differenceAndStore(String key,Collection<Object> otherKeys,String newKey){  
        return redisTemplate.opsForSet().differenceAndStore(newKey, otherKeys, newKey);  
    }  
      
    /** 
     * 删除一个或多个集合中的指定值 
     * @param key 
     * @param values 
     * @return 成功删除数量 
     */  
    public static Long remove(String key,Object ...values){  
        return redisTemplate.opsForSet().remove(key, values);  
    }  
      
    /** 
     * 随机移除一个元素,并返回出来 
     * @param key 
     * @return 
     */  
    public static Object randomSetPop(String key){  
        return redisTemplate.opsForSet().pop(key);  
    }  
      
    /** 
     * 随机获取一个元素 
     * @param key 
     * @return 
     */  
    public static Object randomSet(String key){  
        return redisTemplate.opsForSet().randomMember(key);  
    }  
      
    /** 
     * 随机获取指定数量的元素,同一个元素可能会选中两次 
     * @param key 
     * @param count 
     * @return 
     */  
    public static List<Object> randomSet(String key,long count){  
        return redisTemplate.opsForSet().randomMembers(key, count);  
    }  
      
    /** 
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次) 
     * @param key 
     * @param count 
     * @return 
     */  
    public static Set<Object> randomSetDistinct(String key,long count){  
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);  
    }  
      
    /** 
     * 将 key 中的 value 转入到 destKey 中 
     * @param key 
     * @param value 
     * @param destKey 
     * @return 返回成功与否 
     */  
    public static boolean moveSet(String key,Object value,String destKey){  
        return redisTemplate.opsForSet().move(key, value, destKey);  
    }  
      
    /** 
     * 无序集合的大小 
     * @param key 
     * @return 
     */  
    public static Long setSize(String key){  
        return redisTemplate.opsForSet().size(key);  
    }  
      
    /** 
     * 判断 set 集合中 是否有 value 
     * @param key 
     * @param value 
     * @return 
     */  
    public static boolean isMember(String key,Object value){  
        return redisTemplate.opsForSet().isMember(key, value);  
    }  
      
    /** 
     * 返回 key 和 othere 的并集 
     * @param key 
     * @param otherKey 
     * @return 
     */  
    public static Set<Object> unionSet(String key,String otherKey){  
        return redisTemplate.opsForSet().union(key, otherKey);  
    }  
      
    /** 
     * 返回 key 和 otherKeys 的并集 
     * @param key 
     * @param otherKey key 的集合 
     * @return 
     */  
    public static Set<Object> unionSet(String key,Collection<Object> otherKeys){  
        return redisTemplate.opsForSet().union(key, otherKeys);  
    }  
      
    /** 
     * 将 key 与 otherKey 的并集,保存到 destKey 中 
     * @param key 
     * @param otherKey 
     * @param destKey 
     * @return destKey 数量 
     */  
    public Long unionAndStoreSet(String key, String otherKey,String destKey){  
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);  
    }  
      
    /** 
     * 将 key 与 otherKey 的并集,保存到 destKey 中 
     * @param key 
     * @param otherKeys 
     * @param destKey 
     * @return destKey 数量 
     */  
    public Long unionAndStoreSet(String key, Collection<Object> otherKeys,String destKey){  
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);  
    }  
      
    /** 
     * 返回集合中所有元素 
     * @param key 
     * @return 
     */  
    public Set<Object> members(String key){  
        return redisTemplate.opsForSet().members(key);  
    }  
      
    //Zset 根据 socre 排序   不重复 每个元素附加一个 socre  double类型的属性(double 可以重复)  
      
    /** 
     * 添加 ZSet 元素 
     * @param key 
     * @param value 
     * @param score 
     */  
    public boolean add(String key,Object value,double score){  
        return redisTemplate.opsForZSet().add(key, value, score);  
    }  
      
    /** 
     * 批量添加 Zset <br> 
     *         Set<TypedTuple<Object>> tuples = new HashSet<>();<br> 
     *         TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);<br> 
     *         tuples.add(objectTypedTuple1); 
     * @param key 
     * @param tuples 
     * @return 
     */  
    public Long batchAddZset(String key,Set<TypedTuple<Object>> tuples){  
        return redisTemplate.opsForZSet().add(key, tuples);  
    }  
      
    /** 
     * Zset 删除一个或多个元素 
     * @param key 
     * @param values 
     * @return 
     */  
    public Long removeZset(String key,String ...values){  
        return redisTemplate.opsForZSet().remove(key, values);  
    }  
      
    /** 
     * 对指定的 zset 的 value 值 , socre 属性做增减操作 
     * @param key 
     * @param value 
     * @param score 
     * @return 
     */  
    public Double incrementScore(String key,Object value,double score){  
        return redisTemplate.opsForZSet().incrementScore(key, value, score);  
    }  
      
    /** 
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序) 
     * @param key 
     * @param value 
     * @return 
     */  
    public Long rank(String key,Object value){  
        return redisTemplate.opsForZSet().rank(key, value);  
    }  
      
    /** 
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序) 
     * @param key 
     * @param value 
     * @return 
     */  
    public Long reverseRank(String key,Object value){  
        return redisTemplate.opsForZSet().reverseRank(key, value);  
    }  
      
    /** 
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数) 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public Set<TypedTuple<Object>> rangeWithScores(String key, long start, long end){  
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);  
    }  
      
    /** 
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名) 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public Set<Object> range(String key, long start, long end){  
        return redisTemplate.opsForZSet().range(key, start, end);  
    }  
      
    /** 
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名) 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public Set<Object> rangeByScore(String key, double min, double max){  
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);  
    }  
      
    /** 
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数) 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public Set<TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max){  
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);  
    }  
      
    /** 
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合) 
     * @param key 
     * @param min 
     * @param max 
     * @param offset 从指定下标开始 
     * @param count 输出指定元素数量 
     * @return 
     */  
    public Set<Object> rangeByScore(String key, double min, double max,long offset,long count){  
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);  
    }  
      
    /** 
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合) 
     * @param key 
     * @param min 
     * @param max 
     * @param offset 从指定下标开始 
     * @param count 输出指定元素数量 
     * @return 
     */  
    public Set<TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max,long offset,long count){  
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);  
    }  
      
    /** 
     * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名) 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public Set<Object> reverseRange(String key,long start,long end){  
        return redisTemplate.opsForZSet().reverseRange(key, start, end);  
    }  
      
    /** 
     * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数) 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public Set<TypedTuple<Object>> reverseRangeWithScores(String key,long start,long end){  
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);  
    }  
      
    /** 
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数) 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public Set<Object> reverseRangeByScore(String key,double min,double max){  
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);  
    }  
      
    /** 
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数) 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public Set<TypedTuple<Object>> reverseRangeByScoreWithScores(String key,double min,double max){  
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);  
    }  
      
    /** 
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合) 
     * @param key 
     * @param min 
     * @param max 
     * @param offset 从指定下标开始 
     * @param count 输出指定元素数量 
     * @return 
     */  
    public Set<Object> reverseRangeByScore(String key,double min,double max,long offset,long count){  
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);  
    }  
      
    /** 
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合) 
     * @param key 
     * @param min 
     * @param max 
     * @param offset 从指定下标开始 
     * @param count 输出指定元素数量 
     * @return 
     */  
    public Set<TypedTuple<Object>> reverseRangeByScoreWithScores(String key,double min,double max,long offset,long count){  
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);  
    }  
      
    /** 
     * 返回指定分数区间 [min,max] 的元素个数 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public long countZSet(String key,double min,double max){  
        return redisTemplate.opsForZSet().count(key, min, max);  
    }  
      
    /** 
     * 返回 zset 集合数量 
     * @param key 
     * @return 
     */  
    public long sizeZset(String key){  
        return redisTemplate.opsForZSet().size(key);  
    }  
      
    /** 
     * 获取指定成员的 score 值 
     * @param key 
     * @param value 
     * @return 
     */  
    public Double score(String key,Object value){  
        return redisTemplate.opsForZSet().score(key, value);  
    }  
      
    /** 
     * 删除指定索引位置的成员,其中成员分数按( 从小到大 ) 
     * @param key 
     * @param start 
     * @param end 
     * @return 
     */  
    public Long removeRange(String key,long start ,long end){  
        return redisTemplate.opsForZSet().removeRange(key, start, end);  
    }  
      
    /** 
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 ) 
     * @param key 
     * @param min 
     * @param max 
     * @return 
     */  
    public Long removeRangeByScore(String key,double min ,double max){  
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);  
    }  
      
    /** 
     *  key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加 
     * @param key 
     * @param otherKey 
     * @param destKey 
     * @return 
     */  
    public Long unionAndStoreZset(String key,String otherKey,String destKey){  
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);  
    }  
      
    /** 
     *  key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加 
     * @param key 
     * @param otherKeys 
     * @param destKey 
     * @return 
     */  
    public Long unionAndStoreZset(String key,Collection<String> otherKeys,String destKey){  
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);  
    }  
      
    /** 
     *  key 和 otherKey 两个集合的交集,保存在 destKey 集合中 
     * @param key 
     * @param otherKey 
     * @param destKey 
     * @return 
     */  
    public Long intersectAndStore(String key,String otherKey,String destKey){  
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);  
    }  
      
    /** 
     *  key 和 otherKeys 多个集合的交集,保存在 destKey 集合中 
     * @param key 
     * @param otherKeys 
     * @param destKey 
     * @return 
     */  
    public Long intersectAndStore(String key,Collection<String> otherKeys,String destKey){  
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);  
    }  
    
    /**
     * 加锁
     * @param key   键
     * @param value 当前时间 + 超时时间
     * @return 是否拿到锁
     */
    public static boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            //是否已被别人抢占
            return StringUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue);
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key   键
     * @param value 当前时间 + 超时时间
     */
    public static void unlock(String key, String value) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}  

