package xyz.rasp.laiquendi.processor;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * Class name util
 */
class ClassHelper {

    private String mOriginPackage;
    private String mOriginSimpleName;

    static ClassHelper create(String fullName) {
        ClassHelper classHelper = new ClassHelper();
        int lastPoint = fullName.lastIndexOf(".");
        classHelper.mOriginPackage = fullName.substring(0, lastPoint);
        classHelper.mOriginSimpleName = fullName.substring(lastPoint + 1);
        return classHelper;
    }

    String getPackage() {
        return mOriginPackage;
    }

    String getSimpleName() {
        return mOriginSimpleName;
    }
}
