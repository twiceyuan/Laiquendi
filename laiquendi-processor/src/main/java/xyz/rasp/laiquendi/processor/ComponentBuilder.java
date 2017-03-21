package xyz.rasp.laiquendi.processor;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * 组件 Builder
 */
@SuppressWarnings("WeakerAccess")
public class ComponentBuilder {

    private static final String METHOD_INIT_LAYOUT    = "initLayout";
    private static final String FILED_CONTROLLER      = "mController";
    private static final String GENERATE_CLASS_SUFFIX = "View";

    private String  mOriginClassName;
    private String  mFinalClassName;
    private Element mElement;
    private int     mLayoutId;
    private String  mSimpleName;
    private Class   mSuperClass;

    private ComponentBuilder() {
    }

    public static ComponentBuilder create() {
        return new ComponentBuilder();
    }

    public ComponentBuilder fullName(String originClassName) {
        mOriginClassName = originClassName;
        return this;
    }

    public ComponentBuilder simpleName(String simpleName) {
        mSimpleName = simpleName;
        mFinalClassName = mSimpleName + GENERATE_CLASS_SUFFIX;
        return this;
    }

    public ComponentBuilder element(Element element) {
        mElement = element;
        return this;
    }

    public ComponentBuilder layoutId(int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    public ComponentBuilder superClass(Class superClass) {
        mSuperClass = superClass;
        return this;
    }

    public TypeSpec build() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(mFinalClassName);

        // 基本属性

        if (mSuperClass != null) {
            builder.superclass(TypeName.get(mSuperClass));
        } else {
            builder.superclass(FrameLayout.class);
        }

        builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        // 成员
        buildFields(builder);

        // 构造器
        buildConstructors(builder);

        // 内部原始类获取器
        buildGetter(builder);

        // 初始化布局
        builder.addMethod(MethodSpec.methodBuilder(METHOD_INIT_LAYOUT)
                .addParameter(Context.class, "context")
                .addModifiers(Modifier.PRIVATE)
                // .addCode("android.view.View view = android.view.LayoutInflater.from(context).inflate(" + mLayoutId + ", null, false);\n")
                .addCode("android.view.LayoutInflater.from(context).inflate(" + mLayoutId + ", this, true);\n")
                // .addCode("addView(this);\n")
                // 构造原始控制对象
                .addCode(FILED_CONTROLLER + " = new " + mOriginClassName + "();\n")
                .addCode(FILED_CONTROLLER + ".initView(this);\n")
                .build());

        return builder.build();
    }

    private void buildFields(TypeSpec.Builder builder) {
        builder.addField(FieldSpec.builder(TypeName.get(mElement.asType()), FILED_CONTROLLER)
                .addModifiers(Modifier.PRIVATE)
                .build());
    }

    private void buildGetter(TypeSpec.Builder builder) {

        // 获得原始控制对象
        builder.addMethod(MethodSpec.methodBuilder("get" + mElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(mElement.asType()))
                .addCode("return " + FILED_CONTROLLER + ";\n")
                .build());

        // view 中直接获取原始控制对象
        builder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.get(mElement.asType()))
                .addParameter(View.class, "view")
                .addParameter(int.class, "id")
                .addCode(mFinalClassName + " container = (" + mFinalClassName + ") view.findViewById(id);\n")
                .addCode("return container.get" + mSimpleName + "();\n")
                .build());

        // activity 中获取原始控制对象
        builder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.get(mElement.asType()))
                .addParameter(Activity.class, "activity")
                .addParameter(int.class, "id")
                .addCode(mFinalClassName + " container = (" + mFinalClassName + ") activity.findViewById(id);\n")
                .addCode("return container.get" + mSimpleName + "();\n")
                .build());
    }

    /**
     * 构造 构造器
     */
    private void buildConstructors(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Context.class, "context")
                .addCode("super(context);\n")
                .addCode(METHOD_INIT_LAYOUT + "(context);\n")
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Context.class, "context")
                .addParameter(AttributeSet.class, "attrs")
                .addCode("super(context, attrs);\n")
                .addCode(METHOD_INIT_LAYOUT + "(context);\n")
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Context.class, "context")
                .addParameter(AttributeSet.class, "attrs")
                .addParameter(int.class, "defStyle")
                .addCode("super(context, attrs, defStyle);\n")
                .addCode(METHOD_INIT_LAYOUT + "(context);\n")
                .addModifiers(Modifier.PUBLIC)
                .build());
    }
}
