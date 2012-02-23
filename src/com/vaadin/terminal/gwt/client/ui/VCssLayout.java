/* 
@VaadinApache2LicenseForJavaFiles@
 */

package com.vaadin.terminal.gwt.client.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.StyleConstants;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VCaption;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.VPaintableMap;
import com.vaadin.terminal.gwt.client.VPaintableWidget;
import com.vaadin.terminal.gwt.client.ValueMap;

public class VCssLayout extends SimplePanel implements Container {
    public static final String TAGNAME = "csslayout";
    public static final String CLASSNAME = "v-" + TAGNAME;

    FlowPane panel = new FlowPane();

    Element margin = DOM.createDiv();

    private boolean hasHeight;
    private boolean hasWidth;
    boolean rendering;

    public VCssLayout() {
        super();
        getElement().appendChild(margin);
        setStyleName(CLASSNAME);
        margin.setClassName(CLASSNAME + "-margin");
        setWidget(panel);
    }

    @Override
    protected Element getContainerElement() {
        return margin;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        // panel.setWidth(width);
        hasWidth = width != null && !width.equals("");
        if (!rendering) {
            panel.updateRelativeSizes();
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        // panel.setHeight(height);
        hasHeight = height != null && !height.equals("");
        if (!rendering) {
            panel.updateRelativeSizes();
        }
    }

    public boolean hasChildComponent(Widget component) {
        return panel.hasChildComponent(component);
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        panel.replaceChildComponent(oldComponent, newComponent);
    }

    public class FlowPane extends FlowPanel {

        private final HashMap<Widget, VCaption> widgetToCaption = new HashMap<Widget, VCaption>();
        private ApplicationConnection client;
        private int lastIndex;

        public FlowPane() {
            super();
            setStyleName(CLASSNAME + "-container");
        }

        public void updateRelativeSizes() {
            for (Widget w : getChildren()) {
                if (w instanceof VPaintableWidget) {
                    client.handleComponentRelativeSize(w);
                }
            }
        }

        public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

            // for later requests
            this.client = client;

            final Collection<Widget> oldWidgets = new HashSet<Widget>();
            for (final Iterator<Widget> iterator = iterator(); iterator
                    .hasNext();) {
                oldWidgets.add(iterator.next());
            }

            ValueMap mapAttribute = null;
            if (uidl.hasAttribute("css")) {
                mapAttribute = uidl.getMapAttribute("css");
            }

            lastIndex = 0;
            for (final Iterator<Object> i = uidl.getChildIterator(); i
                    .hasNext();) {
                final UIDL r = (UIDL) i.next();
                final VPaintableWidget child = client.getPaintable(r);
                final Widget widget = child.getWidgetForPaintable();
                if (widget.getParent() == this) {
                    oldWidgets.remove(widget);
                    VCaption vCaption = widgetToCaption.get(widget);
                    if (vCaption != null) {
                        addOrMove(vCaption, lastIndex++);
                        oldWidgets.remove(vCaption);
                    }
                }

                addOrMove(widget, lastIndex++);
                if (mapAttribute != null && mapAttribute.containsKey(r.getId())) {
                    String css = null;
                    try {
                        Style style = widget.getElement().getStyle();
                        css = mapAttribute.getString(r.getId());
                        String[] cssRules = css.split(";");
                        for (int j = 0; j < cssRules.length; j++) {
                            String[] rule = cssRules[j].split(":");
                            if (rule.length == 0) {
                                continue;
                            } else {
                                style.setProperty(
                                        makeCamelCase(rule[0].trim()),
                                        rule[1].trim());
                            }
                        }
                    } catch (Exception e) {
                        VConsole.log("CssLayout encounterd invalid css string: "
                                + css);
                    }
                }

                if (!r.getBooleanAttribute("cached")) {
                    child.updateFromUIDL(r, client);
                }
            }

            // loop oldWidgetWrappers that where not re-attached and unregister
            // them
            for (Widget w : oldWidgets) {
                remove(w);
                VPaintableMap paintableMap = VPaintableMap.get(client);
                if (paintableMap.isPaintable(w)) {
                    final VPaintableWidget p = VPaintableMap.get(client)
                            .getPaintable(w);
                    client.unregisterPaintable(p);
                }
                VCaption vCaption = widgetToCaption.remove(w);
                if (vCaption != null) {
                    remove(vCaption);
                }
            }
        }

        private void addOrMove(Widget child, int index) {
            if (child.getParent() == this) {
                int currentIndex = getWidgetIndex(child);
                if (index == currentIndex) {
                    return;
                }
            }
            insert(child, index);
        }

        public boolean hasChildComponent(Widget component) {
            return component.getParent() == this;
        }

        public void replaceChildComponent(Widget oldComponent,
                Widget newComponent) {
            VCaption caption = widgetToCaption.get(oldComponent);
            if (caption != null) {
                remove(caption);
                widgetToCaption.remove(oldComponent);
            }
            int index = getWidgetIndex(oldComponent);
            if (index >= 0) {
                remove(oldComponent);
                insert(newComponent, index);
            }
        }

        public void updateCaption(VPaintableWidget paintable, UIDL uidl) {
            Widget widget = paintable.getWidgetForPaintable();
            VCaption caption = widgetToCaption.get(widget);
            if (VCaption.isNeeded(uidl, paintable.getState())) {
                if (caption == null) {
                    caption = new VCaption(paintable, client);
                    widgetToCaption.put(widget, caption);
                    insert(caption, getWidgetIndex(widget));
                    lastIndex++;
                } else if (!caption.isAttached()) {
                    insert(caption, getWidgetIndex(widget));
                    lastIndex++;
                }
                caption.updateCaption(uidl);
            } else if (caption != null) {
                remove(caption);
                widgetToCaption.remove(widget);
            }
        }

        VPaintableWidget getComponent(Element element) {
            return Util
                    .getPaintableForElement(client, VCssLayout.this, element);
        }

    }

    private RenderSpace space;

    public RenderSpace getAllocatedSpace(Widget child) {
        if (space == null) {
            space = new RenderSpace(-1, -1) {
                @Override
                public int getWidth() {
                    if (BrowserInfo.get().isIE()) {
                        int width = getOffsetWidth();
                        int margins = margin.getOffsetWidth()
                                - panel.getOffsetWidth();
                        return width - margins;
                    } else {
                        return panel.getOffsetWidth();
                    }
                }

                @Override
                public int getHeight() {
                    int height = getOffsetHeight();
                    int margins = margin.getOffsetHeight()
                            - panel.getOffsetHeight();
                    return height - margins;
                }
            };
        }
        return space;
    }

    public boolean requestLayout(Set<Widget> children) {
        if (hasSize()) {
            return true;
        } else {
            // Size may have changed
            // TODO optimize this: cache size if not fixed, handle both width
            // and height separately
            return false;
        }
    }

    private boolean hasSize() {
        return hasWidth && hasHeight;
    }

    private static final String makeCamelCase(String cssProperty) {
        // TODO this might be cleaner to implement with regexp
        while (cssProperty.contains("-")) {
            int indexOf = cssProperty.indexOf("-");
            cssProperty = cssProperty.substring(0, indexOf)
                    + String.valueOf(cssProperty.charAt(indexOf + 1))
                            .toUpperCase() + cssProperty.substring(indexOf + 2);
        }
        if ("float".equals(cssProperty)) {
            if (BrowserInfo.get().isIE()) {
                return "styleFloat";
            } else {
                return "cssFloat";
            }
        }
        return cssProperty;
    }

    /**
     * Sets CSS classes for margin and spacing based on the given parameters.
     * 
     * @param margins
     *            A {@link VMarginInfo} object that provides info on
     *            top/left/bottom/right margins
     * @param spacing
     *            true to enable spacing, false otherwise
     */
    protected void setMarginAndSpacingStyles(VMarginInfo margins,
            boolean spacing) {
        setStyleName(margin, VCssLayout.CLASSNAME + "-"
                + StyleConstants.MARGIN_TOP, margins.hasTop());
        setStyleName(margin, VCssLayout.CLASSNAME + "-"
                + StyleConstants.MARGIN_RIGHT, margins.hasRight());
        setStyleName(margin, VCssLayout.CLASSNAME + "-"
                + StyleConstants.MARGIN_BOTTOM, margins.hasBottom());
        setStyleName(margin, VCssLayout.CLASSNAME + "-"
                + StyleConstants.MARGIN_LEFT, margins.hasLeft());

        setStyleName(margin, VCssLayout.CLASSNAME + "-" + "spacing", spacing);

    }
}
