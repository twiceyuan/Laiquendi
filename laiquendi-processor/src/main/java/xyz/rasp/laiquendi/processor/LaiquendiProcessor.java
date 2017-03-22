package xyz.rasp.laiquendi.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import xyz.rasp.laiquendi.processor.types.TypeUtil;
import xyz.rasp.laiquendi.processor.types.Types;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * Test
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class LaiquendiProcessor extends AbstractProcessor {

    private Filer                       filer;
    private Elements                    elementUtils;
    private javax.lang.model.util.Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> laiquendi = roundEnvironment.getElementsAnnotatedWith(elementUtils.getTypeElement(Types.ANNOTATION_LAYOUT_ID.toString()));
        for (Element element : laiquendi) {
            int layoutId = (int) TypeUtil.getAnnotationSingleValue(Types.ANNOTATION_LAYOUT_ID, element);
            ClassName parentClass = TypeUtil.getSuperClass(element);
            Name qualifiedName = ((TypeElement) element).getQualifiedName();
            buildViewClass(element, layoutId, qualifiedName.toString(), parentClass);
        }

        return true;
    }

    private void buildViewClass(Element element, int layoutId, String originViewName, ClassName superClass) {

        ClassName className = ClassName.bestGuess(originViewName);

        TypeSpec generatedClassName = ComponentBuilder.create()
                .layoutId(layoutId)
                .element(element)
                .superClass(superClass)
                .fullName(originViewName)
                .simpleName(className.simpleName())
                .build();

        JavaFile javaFile = JavaFile.builder(className.packageName(), generatedClassName)
                .addFileComment("本文件由 Laiquendi 生成，请勿修改")
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Types.ANNOTATION_LAYOUT_ID.toString());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
