package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.SpringServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class MailContentBuilder {

    private TemplateEngine engine;

    @Autowired
    public MailContentBuilder(TemplateEngine engine) {
        this.engine = engine;
    }

    public String build(String title, String message) {
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("message", message);
        return engine.process("mailTemplate", context);
    }
}
