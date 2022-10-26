package cloud.ptl.indexer.api.mail.template;

import cloud.ptl.indexer.api.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class MailTemplate {
    protected final ItemService itemService;
    protected final TemplateEngine templateEngine;
    protected final MessageSource messageSource;

    public MailContent getContent(HashMap params){
        String template = chooseTemplate();
        String title = chooseTitle(params);
        String subject = chooseSubject();

        List<HashMap> items = chooseItems(params);
        if(items == null){
            return null;
        }

        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("items", items);
        ctx.setVariable("title", title);

        final String htmlContent = templateEngine.process(template, ctx);

        return  new MailContent(htmlContent, subject);
    }

    abstract String chooseTemplate();
    abstract String chooseTitle(HashMap params);
    abstract String chooseSubject();
    abstract List<HashMap> chooseItems(HashMap params);
}
