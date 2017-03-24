# Laiquendi

[![](https://jitpack.io/v/twiceyuan/Laiquendi.svg)](https://jitpack.io/#twiceyuan/Laiquendi)

Light-weight UI component wrapper util for Android

轻量级的 UI 组件封装工具，旨在降低根据业务逻辑封装的 UI 组件复杂性。使用 Annotation Processor 生成可在布局文件中预览的 View 代码，方便各种控件的组合和复用。

## 使用场景

* 想使用 include 复用布局，但是想要用 Java 代码来进行一些初始化操作
* 只是想组合多个 View 来实现可复用的控件，却不得不自定义 View 编写很多模板代码
* 需要全局对一些 View 进行属性设置，不想每次都写初始化代码或者在配置布局的属性
* ...

## 引用

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

```
dependencies {
    compile 'com.github.twiceyuan.Laiquendi:laiquendi-core:0.1'
    annotationProcessor 'com.github.twiceyuan.Laiquendi:laiquendi-processor:0.1'
    // 或者使用 apt 'com.github.twiceyuan.Laiquendi:laiquendi-processor:0.1'
}
```

## 使用

例如封装一个通用的 Header 组件（可使用 ButterKnife 进行界面元素的绑定）：

```java
@ComponentId(R.layout.header)
public class Header implements Component {

    @BindView(R.id.tv_back)   TextView mTvBack;
    @BindView(R.id.tv_header) TextView mTvHeader;
    @BindView(R.id.tv_menu)   TextView mTvMenu;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    public void attach(Activity activity) {
        mTvHeader.setText(activity.getTitle());
        mTvBack.setOnClickListener(v -> activity.onBackPressed());
        mTvMenu.setOnClickListener(v -> activity.openOptionsMenu());
    }

    public void setTitle(String title) {
        mTvHeader.setText(title);
    }
}
```

之后增量 Build 一次，就可以生成名为 [组件名] + View 的自定义控件。例如 `Header` 生成的 View 名称就是 HeaderView
布局中直接使用

```xml
<xyz.rasp.laiquendi.sample.HeaderView
    android:id="@+id/header"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

效果图(app:params 为自定义参数，可继承 ParamsComponent 接口来传递一个字符串参数)

![Sample](art/sample.png)

在调用的 Activity 或者 View 中直接获取模块对象

```java
Header header = Eru.get(Header.class, this, R.id.header);
```

## License

```
Copyright 2016 twiceYuan.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
