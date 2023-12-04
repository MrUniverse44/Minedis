package me.blueslime.minedis.utils;

public class PasswordData {
    private final String member;
    private final String nick;
    private final String code;

    private PasswordData(String member, String nick, String code) {
        this.member = member;
        this.nick = nick;
        this.code = code;
    }

    public String getMember() {
        return member;
    }

    public String getNick() {
        return nick;
    }

    public String getCode() {
        return code;
    }

    /**
     * Build a password data
     * @param member of the linked user
     * @param nick of the linked member
     * @param code is the password
     * @return PasswordData
     */
    public static PasswordData build(String member, String nick, String code) {
        return new PasswordData(
                member,
                nick,
                code
        );
    }
}
