package cn.polister.constants;

public class SystemConstants {
    private SystemConstants() {}

    /**
     * 默认登录过期时长
     */
    public static final int LOGIN_TIMEOUT = 1;

    /**
     * Redis中存取文章阅读量Key
     */
    public static final String REDIS_VIEW_COUNT_KEY = "view_count";
}
