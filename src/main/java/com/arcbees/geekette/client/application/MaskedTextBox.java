/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.geekette.client.application;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;

import static com.google.gwt.dom.client.Style.Position.ABSOLUTE;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.css.CSS.LEFT;
import static com.google.gwt.query.client.css.CSS.POSITION;
import static com.google.gwt.query.client.css.Length.px;

/**
 * {@link TextBox} backed by a mask. Characters will always try to replace the first unmatched mask element. If the
 * input doesn't match the mask at the current position, it is ignored. Pasted text will read characters first to last
 * and ignore characters that don't match the mask.
 */
public class MaskedTextBox extends TextBox {
    private static final String PLACEHOLDER_CHAR = "_";
    private static final int PASTE_DELAY_MS = 10;

    private final List<String> regexSegments;
    private final String mask;
    private final GQuery $textArea;

    private String translatedMask;
    private String text;
    private String unparsedText;
    private boolean includePlaceHolderInValue;

    /**
     * @param mask the mask, formatted following these rules:
     * <dl>
     * <dt>#</dt>
     * <dd>[0-9]</dd>
     * <p/>
     * <dt>9</dt>
     * <dd>[\s0-9]</dd>
     * <p/>
     * <dt>A</dt>
     * <dd>[A-Z]</dd>
     * <p/>
     * <dt>a</dt>
     * <dd>[a-z]</dd>
     * <p/>
     * <dt>B</dt>
     * <dd>[A-Za-z]</dd>
     * <p/>
     * <dt>B</dt>
     * <dd>[A-Za-z0-9]</dd>
     * </dl>
     * <p/>
     * The following characters may also be forced: {@code / , * . - : ( ) { } = +} and space. They will be included in
     * the resulting text
     */
    @UiConstructor
    public MaskedTextBox(
            String mask) {
        super();

        this.mask = mask;
        regexSegments = new ArrayList<>();
        includePlaceHolderInValue = true;
        unparsedText = "";

        $textArea = $(DOM.createTextArea());
        $textArea.attr("tabindex", "-1");
        $textArea.css(POSITION.with(ABSOLUTE));
        $textArea.css(LEFT.with(px(-10000)));

        translateMask();
        bindEvents();
    }

    public boolean isIncludePlaceHolderInValue() {
        return includePlaceHolderInValue;
    }

    public void setIncludePlaceHolderInValue(boolean includePlaceHolderInValue) {
        this.includePlaceHolderInValue = includePlaceHolderInValue;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getValue() {
        if (includePlaceHolderInValue && !text.contains(PLACEHOLDER_CHAR)) {
            return text;
        } else {
            return unparsedText;
        }
    }

    @Override
    public void setText(String text) {
        this.text = translatedMask;
        unparsedText = "";

        addText(Strings.nullToEmpty(text));
    }

    private void bindEvents() {
        addAttachHandler(new Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    $textArea.insertAfter(getElement());
                } else {
                    $textArea.remove();
                }
            }
        });

        addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                handleNavigation(event);
            }
        });

        addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                validateAndAddCharCode(event.getCharCode());
                refreshText();

                cancelKey();
            }
        });

        addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                refreshText();
            }
        });
    }

    private void addText(String text) {
        for (char ch : text.toCharArray()) {
            validateAndAddCharCode(ch);
        }

        refreshText();
    }

    private void validateAndAddCharCode(Character ch) {
        if (unparsedText.length() + 1 <= regexSegments.size()
                && ch.toString().matches(regexSegments.get(unparsedText.length()))) {
            unparsedText += ch;
            addCharacter(ch);
        }
    }

    private void handleNavigation(KeyDownEvent event) {
        int keyCode = event.getNativeKeyCode();

        if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_V) {
            onPaste();
        } else if (keyCode == KeyCodes.KEY_BACKSPACE) {
            removeLastCharacter();
            cancelKey();
        } else if (keyCode == KeyCodes.KEY_DELETE) {
            cancelKey();
        }
    }

    private void removeLastCharacter() {
        if (!unparsedText.isEmpty()) {
            unparsedText = unparsedText.substring(0, unparsedText.length() - 1);
        }

        formatAndRefresh();
    }

    /**
     * Capturing pasted text is not natively supported by all browser. The workaround here is:
     * <ol>
     * <li>Listen for {@code ^V}</li>
     * <li>Change the focus to an hidden textarea</li>
     * <li>Wait for a couple milliseconds (so that the text is correctly pasted)</li>
     * <li>Grab the text from this textarea</li>
     * </ol>
     */
    private void onPaste() {
        $textArea.focus();

        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
            @Override
            public boolean execute() {
                String value = $textArea.val();
                $textArea.val("");

                addText(value);
                MaskedTextBox.this.setFocus(true);

                return false;
            }
        }, PASTE_DELAY_MS);
    }

    private void formatAndRefresh() {
        text = translatedMask;

        for (char ch : unparsedText.toCharArray()) {
            addCharacter(ch);
        }

        refreshText();
    }

    private void addCharacter(Character ch) {
        text = text.replaceFirst("[" + PLACEHOLDER_CHAR + "]", String.valueOf(ch));
    }

    private void translateMask() {
        translatedMask = "";

        for (Character ch : mask.toCharArray()) {
            switch (ch) {
                case '#':
                    regexSegments.add("[0-9]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case '9':
                    regexSegments.add("[\\s0-9]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case 'A':
                    regexSegments.add("[A-Z]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case 'a':
                    regexSegments.add("[a-z]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case 'B':
                    regexSegments.add("[A-Za-z]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case 'C':
                    regexSegments.add("[A-Za-z0-9]{1}");
                    translatedMask += PLACEHOLDER_CHAR;
                    break;

                case '/':
                case ',':
                case '*':
                case '.':
                case '-':
                case ':':
                case '(':
                case ')':
                case '{':
                case '}':
                case '=':
                case '+':
                case ' ':
                    translatedMask += ch;
                    break;
                default:
            }
        }

        text = translatedMask;

        super.setText(text);
    }

    private void refreshText() {
        super.setText(text);

        int caretIndex = text.indexOf(PLACEHOLDER_CHAR);
        if (caretIndex == -1) {
            caretIndex = text.length();
        }

        setSelectionRange(caretIndex, 0);
    }
}
