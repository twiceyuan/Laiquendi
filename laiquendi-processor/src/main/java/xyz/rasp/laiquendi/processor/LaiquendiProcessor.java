package xyz.rasp.laiquendi.processor;

import com.google.auto.service.AutoService;
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

import xyz.rasp.laiquendi.core.LaiquendiLayoutId;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * Test
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class LaiquendiProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> laiquendi = roundEnvironment
                .getElementsAnnotatedWith(LaiquendiLayoutId.class);
        for (Element element : laiquendi) {
            int layoutId = element.getAnnotation(LaiquendiLayoutId.class).value();
            Class parentClass = TypeUtil.getSuperClass(element);
            Name qualifiedName = ((TypeElement) element).getQualifiedName();
            buildViewClass(element, layoutId, qualifiedName.toString(), parentClass);
        }

        return true;
    }

    private void buildViewClass(Element element, int layoutId, String originViewName, Class superClass) {

        ClassName className = ClassName.create(originViewName);

        TypeSpec generatedClassName = ComponentBuilder.create()
                .layoutId(layoutId)
                .element(element)
                .superClass(superClass)
                .fullName(originViewName)
                .simpleName(className.getSimpleName())
                .build();

        JavaFile javaFile = JavaFile.builder(className.getPackage(), generatedClassName).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(xyz.rasp.laiquendi.core.LaiquendiLayoutId.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
