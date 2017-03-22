package xyz.rasp.laiquendi.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import xyz.rasp.laiquendi.processor.types.Types;

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

    private String    mOriginClassName;
    private String    mFinalClassName;
    private Element   mElement;
    private int       mLayoutId;
    private String    mSimpleName;
    private ClassName mSuperClass;

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

    public ComponentBuilder superClass(ClassName superClass) {
        mSuperClass = superClass;
        return this;
    }

    public TypeSpec build() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(mFinalClassName);

        // 基本属性

        if (mSuperClass != null) {
            builder.superclass(mSuperClass);
        } else {
            builder.superclass(Types.FRAME_LAYOUT);
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
                .addParameter(Types.CONTEXT, "context")
                .addParameter(Types.ATTRIBUTE_SET, "attrs")
                .addModifiers(Modifier.PRIVATE)
                .addStatement("$T attributes = context.obtainStyledAttributes(attrs, R.styleable.Components)", Types.TYPED_ARRAY)
                .addStatement("String params = attributes.getString(R.styleable.Components_params)")
                .addStatement("attributes.recycle()")
                .addStatement("$T.from(context).inflate($L, this, true)", Types.LAYOUT_INFLATER, mLayoutId)
                // 构造原始控制对象
                .addStatement("$L = new $T()", FILED_CONTROLLER, ClassName.bestGuess(mOriginClassName))
                .addStatement("$L.initView(this)", FILED_CONTROLLER)
                // 初始化参数
                .beginControlFlow("if (params != null && $L instanceof $T)", FILED_CONTROLLER, Types.ANNOTATION_PARAM_COMPONENT)
                .addStatement("onLoadParams(params)")
                .endControlFlow()
                .build());

        // 传递参数
        builder.addMethod(MethodSpec.methodBuilder("onLoadParams")
                .addParameter(String.class, "params")
                .addStatement("(($T) $L).onLoadParams(params)", Types.ANNOTATION_PARAM_COMPONENT, FILED_CONTROLLER)
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
                .addStatement("return " + FILED_CONTROLLER)
                .build());

        // view 中直接获取原始控制对象
        builder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.get(mElement.asType()))
                .addParameter(Types.VIEW, "view")
                .addParameter(int.class, "id")
                .addStatement("$L container = ($L) view.findViewById(id)", mFinalClassName, mFinalClassName)
                .addStatement("return container.get$L()", mSimpleName)
                .build());

        // activity 中获取原始控制对象
        builder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.get(mElement.asType()))
                .addParameter(Types.ACTIVITY, "activity")
                .addParameter(int.class, "id")
                .addStatement("$L container = ($L) activity.findViewById(id)", mFinalClassName, mFinalClassName)
                .addStatement("return container.get$L()", mSimpleName)
                .build());
    }

    /**
     * 构造 构造器
     */
    private void buildConstructors(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Types.CONTEXT, "context")
                .addStatement("super(context)")
                .addStatement(METHOD_INIT_LAYOUT + "(context, null)")
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Types.CONTEXT, "context")
                .addParameter(Types.ATTRIBUTE_SET, "attrs")
                .addStatement("super(context, attrs)")
                .addStatement(METHOD_INIT_LAYOUT + "(context, attrs)")
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(Types.CONTEXT, "context")
                .addParameter(Types.ATTRIBUTE_SET, "attrs")
                .addParameter(int.class, "defStyle")
                .addStatement("super(context, attrs, defStyle)")
                .addStatement(METHOD_INIT_LAYOUT + "(context, attrs)")
                .addModifiers(Modifier.PUBLIC)
                .build());
    }
}
