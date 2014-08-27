package edu.stanford.nlp.international.spanish;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.international.negra.NegraPennLanguagePack;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.Tokenizer;

import edu.stanford.nlp.international.spanish.process.*;

/** @author Christopher Manning
 */
public class SpanishTokenizerTest extends TestCase {

  private final String[] ptbInputs = {
      "Esta es una oración.",
      "¡Dímelo!",
      "Hazlo.",
      "Este es un címbalo.",
      "Metelo.",
      "sub-20",
      "un teléfono (902.400.345).",
      "Port-au-Prince",
      "McLaren/Mercedes",
      "10/12",
      "4X4",
      "3G",
      "3g",
      "sp3",
      "12km",
      "12km/h"
  };

  private final String[][] ptbGold = {
      { "Esta", "es", "una", "oración", "." },
      { "¡", "Di", "me", "lo", "!" },
      { "Haz", "lo", "." },
      { "Este", "es", "un", "címbalo", "." },
      { "Mete", "lo", "." },
      { "sub-20" },
      { "un", "teléfono", "=LRB=", "902.400.345", "=RRB=", "." },
      { "Port", "-", "au", "-", "Prince" },
      { "McLaren", "/", "Mercedes" },
      { "10/12" },
      { "4X4" },
      { "3G" },
      { "3g" },
      { "sp3" },
      { "12", "km" },
      { "12", "km", "/", "h" }
  };

 public void testSpanishTokenizerWord() {
   assert(ptbInputs.length == ptbGold.length);
   final TokenizerFactory<CoreLabel> tf = SpanishTokenizer.ancoraFactory();
   tf.setOptions("");
   tf.setOptions("tokenizeNLs");

     for (int sent = 0; sent < ptbInputs.length; sent++) {
       Tokenizer<CoreLabel> spanishTokenizer = tf.getTokenizer(new StringReader(ptbInputs[sent]));
       int i = 0;
       while (spanishTokenizer.hasNext()) {
	   String w = spanishTokenizer.next().word();
         try {
           assertEquals("SpanishTokenizer problem", ptbGold[sent][i], w);
         } catch (ArrayIndexOutOfBoundsException aioobe) {
           // the assertion below outside the loop will fail
	 }
         i++;
       }
       assertEquals("SpanishTokenizer num tokens problem", i, ptbGold[sent].length);
     }
   }
     /*
  private final String[] corpInputs = {
   "So, too, many analysts predict, will Exxon Corp., Chevron Corp. and Amoco Corp.",
    "So, too, many analysts predict, will Exxon Corp., Chevron Corp. and Amoco Corp.   ",
  // };

  // private final String[][] corpGold = {
  //         { "So", ",", "too", ",", "many", "analysts", "predict", ",", "will", "Exxon",
  //           "Corp.", ",", "Chevron", "Corp.", "and", "Amoco", "Corp", "." }, // strictTreebank3
  //         { "So", ",", "too", ",", "many", "analysts", "predict", ",", "will", "Exxon",
  //                 "Corp.", ",", "Chevron", "Corp.", "and", "Amoco", "Corp.", "." }, // regular
  // };

  // public void testCorp() {
  //   // We test a 2x2 design: {strict, regular} x {no following context, following context}
  //   for (int sent = 0; sent < 4; sent++) {
  //     PTBTokenizer<CoreLabel> ptbTokenizer = new PTBTokenizer<CoreLabel>(new StringReader(corpInputs[sent / 2]),
  //             new CoreLabelTokenFactory(),
  //             (sent % 2 == 0) ? "strictTreebank3": "");
  //     int i = 0;
  //     while (ptbTokenizer.hasNext()) {
  //       CoreLabel w = ptbTokenizer.next();
  //       try {
  //         assertEquals("PTBTokenizer problem", corpGold[sent % 2][i], w.word());
  //       } catch (ArrayIndexOutOfBoundsException aioobe) {
  //         // the assertion below outside the loop will fail
  //       }
  //       i++;
  //     }
  //     if (i != corpGold[sent % 2].length) {
  //       System.out.println("Gold: " + Arrays.toString(corpGold[sent % 2]));
  //       List<CoreLabel> tokens = new PTBTokenizer<CoreLabel>(new StringReader(corpInputs[sent / 2]),
  //             new CoreLabelTokenFactory(),
  //             (sent % 2 == 0) ? "strictTreebank3": "").tokenize();
  //       System.out.println("Guess: " + Sentence.listToString(tokens));
  //       System.out.flush();
  //     }
  //     assertEquals("PTBTokenizer num tokens problem", i, corpGold[sent % 2].length);
  //   }
  // }

  // public void testJacobEisensteinApostropheCase() {
  //   StringReader reader = new StringReader("it's");
  //   PTBTokenizer<Word> tokenizer = PTBTokenizer.newPTBTokenizer(reader);
  //   List<Word> stemmedTokens = tokenizer.tokenize();
  //   // for (Word word : stemmedTokens) System.out.print (word+" ");
  //   reader = new StringReader(" it's ");
  //   tokenizer = PTBTokenizer.newPTBTokenizer(reader);
  //   List<Word> stemmedTokens2 = tokenizer.tokenize();
  //   // System.out.println ();
  //   // for (Word word : stemmedTokens2) System.out.print (word+" ");
  //   // System.out.println();
  //   assertEquals(stemmedTokens, stemmedTokens2);
  // }

  // private final static String[] untokInputs = {
  //   "London - AFP reported junk .",
  //   "Paris - Reuters reported news .",
  //   "Sydney - News said - something .",
  //   "HEADLINE - New Android phone !",
  //   "I did it 'cause I wanted to , and you 'n' me know that .",
  //   "He said that `` Luxembourg needs surface - to - air missiles . ''",
  // };

  // private final static String[] untokOutputs = {
  //   "London - AFP reported junk.",
  //   "Paris - Reuters reported news.",
  //   "Sydney - News said - something.",
  //   "HEADLINE - New Android phone!",
  //   "I did it 'cause I wanted to, and you 'n' me know that.",
  //   "He said that \"Luxembourg needs surface-to-air missiles.\"",
  // };


  // public void testUntok() {
  //   assert(untokInputs.length == untokOutputs.length);
  //   for (int i = 0; i < untokInputs.length; i++) {
  //     assertEquals("untok gave the wrong result", untokOutputs[i], PTBTokenizer.ptb2Text(untokInputs[i]));
  //   }
  // }


  // Public Void Testinvertible() {
  //   String text = "  This     is     a      colourful sentence.    ";
  //   PTBTokenizer<CoreLabel> tokenizer =
  //     PTBTokenizer.newPTBTokenizer(new StringReader(text), false, true);
  //   List<CoreLabel> tokens = tokenizer.tokenize();
  //   assertEquals(6, tokens.size());
  //   assertEquals("  ", tokens.get(0).get(CoreAnnotations.BeforeAnnotation.class));
  //   assertEquals("     ", tokens.get(0).get(CoreAnnotations.AfterAnnotation.class));
  //   assertEquals("Wrong begin char offset", 2, (int) tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
  //   assertEquals("Wrong end char offset", 6, (int) tokens.get(0).get(CoreAnnotations.CharacterOffsetEndAnnotation.class));
  //   assertEquals("This", tokens.get(0).get(CoreAnnotations.OriginalTextAnnotation.class));
  //   // note: after(x) and before(x+1) are the same
  //   assertEquals("     ", tokens.get(0).get(CoreAnnotations.AfterAnnotation.class));
  //   assertEquals("     ", tokens.get(1).get(CoreAnnotations.BeforeAnnotation.class));
  //   // americanize is now off by default
  //   assertEquals("colourful", tokens.get(3).get(CoreAnnotations.TextAnnotation.class));
  //   assertEquals("colourful", tokens.get(3).get(CoreAnnotations.OriginalTextAnnotation.class));
  //   assertEquals("", tokens.get(4).after());
  //   assertEquals("", tokens.get(5).before());
  //   assertEquals("    ", tokens.get(5).get(CoreAnnotations.AfterAnnotation.class));

  //   StringBuilder result = new StringBuilder();
  //   result.append(tokens.get(0).get(CoreAnnotations.BeforeAnnotation.class));
  //   for (CoreLabel token : tokens) {
  //     result.append(token.get(CoreAnnotations.OriginalTextAnnotation.class));
  //     String after = token.get(CoreAnnotations.AfterAnnotation.class);
  //     if (after != null)
  //       result.append(after);
  //   }
  //   assertEquals(text, result.toString());

  //   for (int i = 0; i < tokens.size() - 1; ++i) {
  //     assertEquals(tokens.get(i).get(CoreAnnotations.AfterAnnotation.class),
  //                  tokens.get(i + 1).get(CoreAnnotations.BeforeAnnotation.class));
  //   }
  // }

  // private final String[] sgmlInputs = {
  //   "Significant improvements in peak FEV1 were demonstrated with tiotropium/olodaterol 5/2 μg (p = 0.008), 5/5 μg (p = 0.012), and 5/10 μg (p < 0.0001) versus tiotropium monotherapy [51].",
  //   "Panasonic brand products are produced by Samsung Electronics Co. Ltd. Sanyo products aren't.",
  //   "Oesophageal acid exposure (% time <pH 4) was similar in patients with or without complications (19.2% v 19.3% p>0.05).",
  //   "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Strict//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">",
  //   "Hi! <foo bar=\"baz xy = foo !$*) 422\" > <?PITarget PIContent?> <?PITarget PIContent> Hi!",
  //   "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>\n<book xml:id=\"simple_book\" xmlns=\"http://docbook.org/ns/docbook\" version=\"5.0\">\n",
  //   "<chapter xml:id=\"chapter_1\"><?php echo $a; ?>\n<!-- This is an SGML/XML comment \"Hi!\" -->\n<p> </p> <p-fix / >",
  //   "<a href=\"http:\\\\it's\\here\"> <quote orig_author='some \"dude'/> <not sgmltag",
  //   "<quote previouspost=\"\n" +
  //           "&gt; &gt; I really don't want to process this junk.\n" +
  //           "&gt; No one said you did, runny.  What's got you so scared, anyway?-\n" +
  //           "\">",
  //   "&lt;b...@canada.com&gt; funky@thedismalscience.net <myemail@where.com>",
  // };

  // private final String[][] sgmlGold = {
  //   { "Significant", "improvements", "in", "peak", "FEV1", "were", "demonstrated", "with", "tiotropium/olodaterol",
  //           "5/2", "μg", "-LRB-", "p", "=", "0.008", "-RRB-", ",", "5/5", "μg", "-LRB-", "p", "=", "0.012", "-RRB-",
  //           ",", "and", "5/10", "μg", "-LRB-", "p", "<", "0.0001", "-RRB-", "versus", "tiotropium", "monotherapy",
  //           "-LSB-", "51", "-RSB-", "." },
  //   { "Panasonic", "brand", "products", "are", "produced", "by", "Samsung", "Electronics", "Co.", "Ltd.", ".",
  //           "Sanyo", "products", "are", "n't", ".", },
  //   { "Oesophageal", "acid", "exposure", "-LRB-", "%", "time", "<", "pH", "4", "-RRB-", "was", "similar", "in",
  //           "patients", "with", "or", "without", "complications", "-LRB-", "19.2", "%", "v", "19.3", "%",
  //           "p", ">", "0.05", "-RRB-", ".", },
  //   { "<!DOCTYPE\u00A0html\u00A0PUBLIC\u00A0\"-//W3C//DTD\u00A0HTML\u00A04.01\u00A0Strict//EN\"\u00A0\"http://www.w3.org/TR/html4/strict.dtd\">" }, // spaces go to &nbsp; \u00A0
  //   { "Hi", "!", "<foo\u00A0bar=\"baz\u00A0xy\u00A0=\u00A0foo\u00A0!$*)\u00A0422\"\u00A0>", "<?PITarget\u00A0PIContent?>", "<?PITarget\u00A0PIContent>", "Hi", "!" },
  //   { "<?xml\u00A0version=\"1.0\"\u00A0encoding=\"UTF-8\"\u00A0?>", "<?xml-stylesheet\u00A0type=\"text/xsl\"\u00A0href=\"style.xsl\"?>",
  //           "<book\u00A0xml:id=\"simple_book\"\u00A0xmlns=\"http://docbook.org/ns/docbook\"\u00A0version=\"5.0\">", },
  //   { "<chapter\u00A0xml:id=\"chapter_1\">", "<?php\u00A0echo\u00A0$a;\u00A0?>", "<!--\u00A0This\u00A0is\u00A0an\u00A0SGML/XML\u00A0comment\u00A0\"Hi!\"\u00A0-->",
  //           "<p>", "</p>", "<p-fix\u00A0/\u00A0>"},
  //   { "<a href=\"http:\\\\it's\\here\">", "<quote orig_author='some \"dude'/>", "<", "not", "sgmltag" },
  //   { "<quote previouspost=\"\n" +
  //           "&gt; &gt; I really don't want to process this junk.\n" +
  //           "&gt; No one said you did, runny.  What's got you so scared, anyway?-\n" +
  //           "\">" },
  //   { "&lt;b...@canada.com&gt;", "funky@thedismalscience.net", "<myemail@where.com>" },
  // };

  // public void testPTBTokenizerSGML() {
  //   assert(sgmlInputs.length == sgmlGold.length);
  //   for (int sent = 0; sent < sgmlInputs.length; sent++) {
  //     PTBTokenizer<CoreLabel> ptbTokenizer =
  //             new PTBTokenizer<CoreLabel>(new StringReader(sgmlInputs[sent]), new CoreLabelTokenFactory(), "");
  //     for (int i = 0; ptbTokenizer.hasNext() || i < sgmlGold[sent].length; i++) {
  //       if ( ! ptbTokenizer.hasNext()) {
  //         fail("PTBTokenizer generated too few tokens for sentence " + sent + "! Missing " + sgmlGold[sent][i]);
  //       }
  //       CoreLabel w = ptbTokenizer.next();
  //       try {
  //         assertEquals("PTBTokenizer problem", sgmlGold[sent][i], w.value());
  //       } catch (ArrayIndexOutOfBoundsException aioobe) {
  //         fail("PTBTokenizer generated too many tokens for sentence " + sent + "! Added " + w.value());
  //       }
  //     }
  //   }
  // }

  // public void testPTBTokenizerGerman() {
  //   String sample = "Das TV-Duell von Kanzlerin Merkel und SPD-Herausforderer Steinbrück war eher lahm - können es die Spitzenleute der kleinen Parteien besser? " +
  //           "Die erquickende Sicherheit und Festigkeit in der Bewegung, den Vorrat von Kraft, kann ja die Versammlung nicht fühlen, hören will sie sie nicht, also muß sie sie sehen; und die sehe man einmal in einem Paar spitzen Schultern, zylindrischen Schenkeln, oder leeren Ärmeln, oder lattenförmigen Beinen.";
  //   String[] tokenized = {
  //       "Das", "TV-Duell", "von", "Kanzlerin", "Merkel", "und", "SPD-Herausforderer", "Steinbrück", "war", "eher",
  //       "lahm", "-", "können", "es", "die", "Spitzenleute", "der", "kleinen", "Parteien", "besser", "?",
  //       "Die", "erquickende", "Sicherheit", "und", "Festigkeit", "in", "der", "Bewegung", ",", "den", "Vorrat", "von",
  //       "Kraft", ",", "kann", "ja", "die", "Versammlung", "nicht", "fühlen", ",", "hören", "will", "sie", "sie",
  //       "nicht", ",", "also", "muß", "sie", "sie", "sehen", ";", "und", "die", "sehe", "man", "einmal", "in", "einem",
  //       "Paar", "spitzen", "Schultern", ",", "zylindrischen", "Schenkeln", ",", "oder", "leeren", "Ärmeln", ",",
  //       "oder", "lattenförmigen", "Beinen", "."

  //   };
  //   TreebankLanguagePack tlp = new NegraPennLanguagePack();
  //   Tokenizer<? extends HasWord> toke =tlp.getTokenizerFactory().getTokenizer(new StringReader(sample));
    List<? extends HasWord> tokens = toke.tokenize();
    List<? extends HasWord> goldTokens = Sentence.toWordList(tokenized);
    assertEquals("Tokenization length mismatch", goldTokens.size(), tokens.size());
    for (int i = 0, sz = goldTokens.size(); i < sz; i++) {
      assertEquals("Bad tokenization", goldTokens.get(i).word(), tokens.get(i).word());
    }
  }

  private final String[] mtInputs = {
    "Enter an option [?/Current]:{1}",
    "for example, {1}http://www.autodesk.com{2}, or a path",
    "enter {3}@{4} at the Of prompt.",
    "{1}block name={2}",
  };

  private final String[][] mtGold = {
    { "Enter", "an", "option", "-LSB-", "?", "/", "Current", "-RSB-", ":", "-LCB-", "1", "-RCB-" },
    { "for", "example", ",", "-LCB-", "1", "-RCB-", "http://www.autodesk.com", "-LCB-", "2", "-RCB-", ",", "or", "a", "path" },
    { "enter", "-LCB-", "3", "-RCB-", "@", "-LCB-", "4", "-RCB-", "at", "the", "Of", "prompt", "." },
    { "-LCB-", "1", "-RCB-", "block", "name", "=", "-LCB-", "2", "-RCB-" },
  };

  public void testPTBTokenizerMT() {
    assert(mtInputs.length == mtGold.length);
    for (int sent = 0; sent < mtInputs.length; sent++) {
      PTBTokenizer<Word> ptbTokenizer = PTBTokenizer.newPTBTokenizer(new StringReader(mtInputs[sent]));
      int i = 0;
      while (ptbTokenizer.hasNext()) {
        Word w = ptbTokenizer.next();
        try {
          assertEquals("PTBTokenizer problem on string " + sent + " token " + i, mtGold[sent][i], w.value());
        } catch (ArrayIndexOutOfBoundsException aioobe) {
          // the assertion below outside the loop will fail
        }
        i++;
      }
      assertEquals("PTBTokenizer num tokens problem for case " + sent, i, mtGold[sent].length);
    }
  }
    */
}