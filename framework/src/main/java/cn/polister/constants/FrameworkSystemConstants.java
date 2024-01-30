package cn.polister.constants;

public class FrameworkSystemConstants {
    private FrameworkSystemConstants() {}

    /**
     * 默认登录过期时长
     */
    public static final int LOGIN_TIMEOUT = 1;

    /**
     * Redis中存取文章阅读量Key
     */
    public static final String REDIS_VIEW_COUNT_KEY = "view_count";
    /**
     * 角色状态正常
     */
    public static final String ROLE_STATUS_NORMAL = "0";
    /**
     *  友链状态正常
     */
    public static final String LINK_STATUS_NORMAL = "0";
}
