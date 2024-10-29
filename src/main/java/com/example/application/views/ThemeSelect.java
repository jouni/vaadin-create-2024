package com.example.application.views;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;

@CssImport("./components/theme-select.css")
public class ThemeSelect extends Select<String> {
  public ThemeSelect() {
    setAriaLabel("Choose theme");
    setId("theme-select");
    addClassNames("theme-select");
    setOverlayClassName("theme-select");

    setItems("system", "light", "dark");

    setRenderer(new ComponentRenderer<>(theme -> {
      SvgIcon icon = new SvgIcon("/themes/my-theme/icons/theme-" + theme.toLowerCase() + ".svg");
      Div wrapper = new Div(icon, new Span(theme));
      return wrapper;
    }));

    getElement().executeJs("return localStorage.getItem('theme') || 'system'").then(String.class, theme -> {
      setValue(theme);
    });

    addValueChangeListener(event -> {
      String theme = event.getValue();

      getElement().executeJs("""
        let theme = $0;
        if (theme == 'system') {
          localStorage.removeItem('theme');
        } else {
          localStorage.setItem('theme', theme);
        }
        updateTheme();
        """, theme);
    });
  }

  @ClientCallable
  public void setValue(String theme) {
    super.setValue(theme);
  }
}
