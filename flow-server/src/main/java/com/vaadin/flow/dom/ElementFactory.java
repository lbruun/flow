/*
 * Copyright 2000-2022 Vaadin Ltd.
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
package com.vaadin.flow.dom;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.shared.ApplicationConstants;

/**
 * Helpers to create {@link Element} instances.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public interface ElementFactory {

    /**
     * Creates an {@code <a>} element.
     *
     * @return an {@code <a>} element.
     */
    static Element createAnchor() {
        return new Element(Tag.A);
    }

    /**
     * Creates an {@code <a>} with the given {@code href} attribute.
     *
     * @param href
     *            the href attribute for the link
     * @return an {@code <a>} element.
     */
    static Element createAnchor(String href) {
        return createAnchor().setAttribute("href", href);
    }

    /**
     * Creates an {@code <a>} element with the given {@code href} attribute
     * and text content.
     *
     * @param href
     *            the href parameter for the element
     * @param textContent
     *            the text content of the element
     * @return an {@code <a>} element.
     */
    static Element createAnchor(String href, String textContent) {
        return createAnchor(href).setText(textContent);
    }

    /**
     * Creates an {@code <a>} element with the given {@code href} attribute,
     * text content and the router link attribute. Router links are handled by
     * the framework to perform view navigation without a page reload.
     *
     * @param href
     *            the href parameter for the element
     * @param textContent
     *            the text content of the element
     * @return an {@code <a>} element.
     */
    static Element createRouterLink(String href, String textContent) {
        return createAnchor(href, textContent)
                .setAttribute(ApplicationConstants.ROUTER_LINK_ATTRIBUTE, "");
    }

    /**
     * Creates a {@code <br>} element.
     *
     * @return a {@code <br>} element.
     */
    static Element createBr() {
        return new Element(Tag.BR);
    }

    /**
     * Creates a {@code <button>} element.
     *
     * @return a {@code <button>} element.
     */
    static Element createButton() {
        return new Element(Tag.BUTTON);
    }

    /**
     * Creates a {@code <button>} with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <button>} element.
     */
    static Element createButton(String textContent) {
        return createButton().setText(textContent);
    }

    /**
     * Creates a {@code <div>} element.
     *
     * @return a {@code <div>} element.
     */
    static Element createDiv() {
        return new Element(Tag.DIV);
    }

    /**
     * Creates a {@code <div>} with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <div>} element.
     */
    static Element createDiv(String textContent) {
        return createDiv().setText(textContent);
    }

    /**
     * Creates an {@code <h1>} element.
     *
     * @return an {@code <h1>} element.
     */
    static Element createHeading1() {
        return new Element(Tag.H1);
    }

    /**
     * Creates an {@code <h2>} element.
     *
     * @return an {@code <h2>} element.
     */
    static Element createHeading2() {
        return new Element(Tag.H2);
    }

    /**
     * Creates an {@code <h3>} element.
     *
     * @return an {@code <h3>} element.
     */
    static Element createHeading3() {
        return new Element(Tag.H3);
    }

    /**
     * Creates an {@code <h4>} element.
     *
     * @return an {@code <h4>} element.
     */
    static Element createHeading4() {
        return new Element(Tag.H4);
    }

    /**
     * Creates an {@code <h5>} element.
     *
     * @return an {@code <h5>} element.
     */
    static Element createHeading5() {
        return new Element(Tag.H5);
    }

    /**
     * Creates an {@code <h6>} element.
     *
     * @return an {@code <h6>} element.
     */
    static Element createHeading6() {
        return new Element(Tag.H6);
    }

    /**
     * Creates a {@code <h1>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h1>} element.
     */
    static Element createHeading1(String textContent) {
        return createHeading1().setText(textContent);
    }

    /**
     * Creates a {@code <h2>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h2>} element.
     */
    static Element createHeading2(String textContent) {
        return createHeading2().setText(textContent);
    }

    /**
     * Creates a {@code <h3>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h3>} element.
     */
    static Element createHeading3(String textContent) {
        return createHeading3().setText(textContent);
    }

    /**
     * Creates a {@code <h4>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h4>} element.
     */
    static Element createHeading4(String textContent) {
        return createHeading4().setText(textContent);
    }

    /**
     * Creates a {@code <h5>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h5>} element.
     */
    static Element createHeading5(String textContent) {
        return createHeading5().setText(textContent);
    }

    /**
     * Creates a {@code <h6>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <h6>} element.
     */
    static Element createHeading6(String textContent) {
        return createHeading6().setText(textContent);
    }

    /**
     * Creates an {@code <hr>} element.
     *
     * @return an {@code <hr>} element.
     */
    static Element createHr() {
        return new Element(Tag.HR);
    }

    /**
     * Creates an {@code <input>} element.
     *
     * @return an {@code <input>} element.
     */
    static Element createInput() {
        return new Element(Tag.INPUT);
    }

    /**
     * Creates an {@code <input>} element with the given type.
     *
     * @param type
     *            the type attribute for the element
     * @return an {@code <input>} element
     */
    static Element createInput(String type) {
        return new Element(Tag.INPUT).setAttribute("type", type);
    }

    /**
     * Creates an {@code <label>} element.
     *
     * @return an {@code <label>} element.
     */
    static Element createLabel() {
        return new Element(Tag.LABEL);
    }

    /**
     * Creates an {@code <label>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <label>} element.
     */
    static Element createLabel(String textContent) {
        return createLabel().setText(textContent);
    }

    /**
     * Creates an {@code <li>} element.
     *
     * @return an {@code <li>} element.
     */
    static Element createListItem() {
        return new Element(Tag.LI);
    }

    /**
     * Creates an {@code <li>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <li>} element.
     */
    static Element createListItem(String textContent) {
        return createListItem().setText(textContent);
    }

    /**
     * Creates an {@code <option>} element.
     *
     * @return an {@code <option>} element.
     */
    static Element createOption() {
        return new Element(Tag.OPTION);
    }

    /**
     * Creates an {@code <option>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <option>} element.
     */
    static Element createOption(String textContent) {
        return createOption().setText(textContent);
    }

    /**
     * Creates a {@code <p>} element.
     *
     * @return a {@code <p>} element.
     */
    static Element createParagraph() {
        return new Element(Tag.P);
    }

    /**
     * Creates a {@code <p>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <p>} element.
     */
    static Element createParagraph(String textContent) {
        return new Element(Tag.P).setText(textContent);
    }

    /**
     * Creates a {@code <pre>} element.
     *
     * @return a {@code <pre>} element.
     */
    static Element createPreformatted() {
        return new Element(Tag.PRE);
    }

    /**
     * Creates a {@code <pre>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <pre>} element.
     */
    static Element createPreformatted(String textContent) {
        return createPreformatted().setText(textContent);
    }

    /**
     * Creates a {@code <select>} element.
     *
     * @return a {@code <select>} element.
     */
    static Element createSelect() {
        return new Element(Tag.SELECT);
    }

    /**
     * Creates a {@code <span>} element.
     *
     * @return a {@code <span>} element.
     */
    static Element createSpan() {
        return new Element(Tag.SPAN);
    }

    /**
     * Creates a {@code <span>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <span>} element.
     */
    static Element createSpan(String textContent) {
        return createSpan().setText(textContent);
    }

    /**
     * Creates a {@code <textarea>} element.
     *
     * @return a {@code <textarea>} element.
     */
    static Element createTextarea() {
        return new Element(Tag.TEXTAREA);
    }

    /**
     * Creates a {@code <ul>} element.
     *
     * @return a {@code <ul>} element.
     */
    static Element createUnorderedList() {
        return new Element(Tag.UL);
    }

    /**
     * Creates a {@code <strong>} element.
     *
     * @return a {@code <strong>} element.
     */
    static Element createStrong() {
        return new Element(Tag.STRONG);
    }

    /**
     * Creates a {@code <strong>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return a {@code <strong>} element
     */
    static Element createStrong(String textContent) {
        return createStrong().setText(textContent);
    }

    /**
     * Creates an {@code <em>} element.
     *
     * @return an {@code <em>} element.
     */
    static Element createEmphasis() {
        return new Element(Tag.EM);
    }

    /**
     * Creates an {@code <em>} element with the given text content.
     *
     * @param textContent
     *            the text content of the element
     * @return an {@code <em>} element.
     */
    static Element createEmphasis(String textContent) {
        return createEmphasis().setText(textContent);
    }

}
