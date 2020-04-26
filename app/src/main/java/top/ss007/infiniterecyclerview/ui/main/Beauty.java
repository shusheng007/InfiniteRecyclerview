package top.ss007.infiniterecyclerview.ui.main;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2020/4/9 17:41
 * @description
 */
public class Beauty {
    private String name;
    private int photoId;
    private String introduction;

    public Beauty(String name, int photoId, String introduction) {
        this.name = name;
        this.photoId = photoId;
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "Beauty{" +
                "name='" + name + '\'' +
                ", photoUrl='" + photoId + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
