package xyz.rasp.laiquendi.processor;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * Class name util
 */
class ClassName {

    private String mOriginPackage;
    private String mOriginSimpleName;

    static ClassName create(String fullName) {
        ClassName className = new ClassName();
        int lastPoint = fullName.lastIndexOf(".");
        className.mOriginPackage = fullName.substring(0, lastPoint);
        className.mOriginSimpleName = fullName.substring(lastPoint + 1);
        return className;
    }

    String getPackage() {
        return mOriginPackage;
    }

    String getSimpleName() {
        return mOriginSimpleName;
    }
}
