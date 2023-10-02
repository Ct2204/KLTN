
package kltn.productservice.seller.service.impl;



import kltn.productservice.seller.service.SlugService;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class SlugServiceImpl implements SlugService {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Override
    public String createSlug(String productName) {
        String normalized = Normalizer.normalize(productName, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        slug = slug.toLowerCase(Locale.ENGLISH);
        slug = addTimestampOrRandomString(slug);
        return slug;
    }

    @Override
    public String addTimestampOrRandomString(String slug) {

         // Append a random string
         String randomString = UUID.randomUUID().toString().substring(0, 8);
         String uniqueSlug = slug + "-" + randomString;
        return uniqueSlug;
    }
}
