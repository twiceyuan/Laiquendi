package xyz.rasp.laiquendi.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import xyz.rasp.laiquendi.processor.types.Constants;
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
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(mFinalClassName);
        classBuilder.addJavadoc("Generate by {@link $L}. \n", mOriginClassName);

        // 基本属性

        if (mSuperClass != null) {
            classBuilder.superclass(mSuperClass);
        } else {
            classBuilder.superclass(Types.FRAME_LAYOUT);
        }

        classBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        // 成员
        buildFields(classBuilder);

        // 构造器
        buildConstructors(classBuilder);

        // 内部原始类获取器
        buildGetter(classBuilder);

        // 初始化布局
        MethodSpec.Builder initLayoutBuilder = MethodSpec.methodBuilder(METHOD_INIT_LAYOUT)
                .addAnnotation(Types.RESOURCE_TYPE_WARNING)
                .addParameter(Types.CONTEXT, "context")
                .addParameter(Types.ATTRIBUTE_SET, "attrs")
                .addModifiers(Modifier.PRIVATE)
                .addStatement("$T attributes = context.obtainStyledAttributes(attrs, R.styleable.Components)", Types.TYPED_ARRAY)
                .addStatement("String params = attributes.getString($T.styleable.Components_params)", Types.R)
                .addStatement("attributes.recycle()");

        // 判断是否存在布局并且填充
        if (mLayoutId != Constants.NO_ID) {
            initLayoutBuilder.addStatement("$T.from(context).inflate($L, this, true)", Types.LAYOUT_INFLATER, mLayoutId);
        }

        // 构造原始控制对象
        initLayoutBuilder.addStatement("$L = new $T()", FILED_CONTROLLER, ClassName.bestGuess(mOriginClassName))
                .beginControlFlow("if ($L instanceof $T)", FILED_CONTROLLER, Types.INTERFACE_COMPONENT_CREATE)
                .addStatement("(($T) $L).$L(this)",
                        Types.INTERFACE_COMPONENT_CREATE,
                        FILED_CONTROLLER,
                        Types.INTERFACE_COMPONENT_CREATE_METHOD)
                .endControlFlow()
                // 初始化参数
                .beginControlFlow("if (params != null && $L instanceof $T)", FILED_CONTROLLER, Types.INTERFACE_PARAMS_LOAD)
                .addStatement("(($T) $L).$L(params)",
                        Types.INTERFACE_PARAMS_LOAD,
                        FILED_CONTROLLER,
                        Types.INTERFACE_PARAMS_LOAD_METHOD)
                .endControlFlow();

        classBuilder.addMethod(initLayoutBuilder.build());

        return classBuilder.build();
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
    }
}
