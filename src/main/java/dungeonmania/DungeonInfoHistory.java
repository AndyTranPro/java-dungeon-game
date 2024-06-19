package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DungeonInfoHistory implements Serializable{
    private List<DungeonInfo> dungeonInfoHistory = new ArrayList<>();

    public void addDungeonInfo(DungeonInfo dungeonInfo) {
        dungeonInfoHistory.add(dungeonInfo);
    }

    public DungeonInfo getDungeonInfo(int index) {
        return dungeonInfoHistory.get(index);
    }

    public List<DungeonInfo> getDungeonInfoList() {
        return dungeonInfoHistory;
    }

    public int getDungeonInfoSize() {
        return dungeonInfoHistory.size();
    }
}
