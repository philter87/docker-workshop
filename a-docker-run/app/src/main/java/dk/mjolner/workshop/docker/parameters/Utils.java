package dk.mjolner.workshop.docker.parameters;

import j2html.tags.DomContent;
import j2html.tags.specialized.HeadTag;
import j2html.tags.specialized.HtmlTag;
import j2html.tags.specialized.LiTag;

import static j2html.TagCreator.*;

public class Utils {
    public static LiTag bullet(String text, boolean isSuccessful) {
        var color = isSuccessful ? "background-color: #ABEBC6" : "background-color: #F5B7B1";
        return li(h3(span(text).withStyle(color)));
    }

    public static HeadTag header() {
        return head(
                title("Docker Workshop"),
                style("@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@100&display=swap');" +
                        "html * {font-family: 'Roboto', sans-serif}")
        );
    }

    public static String page(DomContent... content) {
        return html(
                head(
                        title("Docker Workshop"),
                        style("@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@100&display=swap');" +
                                "html * {font-family: 'Roboto', sans-serif}")
                ),
                body(content)
        ).render();
    };
}
