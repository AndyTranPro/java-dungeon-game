package dungeonmania.response.models;

import java.io.Serializable;

public final class ItemResponse implements Serializable{
    private final String id;
    private final String type;

    public ItemResponse(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final String getId() {
        return id;
    }
}
