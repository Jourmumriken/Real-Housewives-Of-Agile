/**
 * The Account class represents a user account.
 * Each account has a username and password, and allows
 * for checking whether inputs match the user's password or not.
 */
public class Account {
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Asserts that given string matches password
     * 
     * @param atempt string that will be matched to password
     * @return true if argument matches password, else false
     */
    public boolean checkPw(String atempt) {
        char[] arr = atempt.toCharArray();
        char[] pw = password.toCharArray();
        try {
            for (int i = 0; i < pw.length; i++) {
                if (pw[i] != arr[i]) {
                    return false;
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Asserts that one Account object is the same as another.
     * 
     * @param o the object to compare to this.
     * @return true if the names match, as they are unique.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Account a = (Account) o;
        if (username.equals(a.username))
            return true;
        return false;
    }
}
