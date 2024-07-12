package whatap.lang.slog;

public class Field {
    public String key;
    public String value;

    public Field(String key, StringBuilder value) {
        this(key, value.toString());
    }

    public Field(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}
