package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Util.RedisAdapter;
import shu.chl.toutiao.Util.RedisKeyUtil;

@Service
public class LikeService {
    @Autowired
    RedisAdapter redisAdapter;
    //返回喜欢人数
    public long likeNews(int entityId,int entityType,int userId){
        String likeKey=RedisKeyUtil.getLikeKey(entityId,entityType);
        redisAdapter.sadd(likeKey,String.valueOf(userId));
        //点喜欢的同时，清除不喜欢队列
        String disKey=RedisKeyUtil.getDisLikeKey(entityId,entityType);
        redisAdapter.srem(disKey,String.valueOf(userId));
        return redisAdapter.scard(likeKey);
    }
    public long disLikeNews(int entityId,int entityType,int userId){
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityId,entityType);
        redisAdapter.sadd(disLikeKey,String.valueOf(userId));
        //点喜欢的同时，清除不喜欢队列
        String likeKey=RedisKeyUtil.getLikeKey(entityId,entityType);
        redisAdapter.srem(likeKey,String.valueOf(userId));
        return redisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(redisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return redisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

}
