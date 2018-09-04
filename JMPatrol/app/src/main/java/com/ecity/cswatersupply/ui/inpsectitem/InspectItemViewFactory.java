package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.HashMap;
import java.util.Map;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;

public class InspectItemViewFactory {
    private static final Map<EInspectItemType, Class<?>> inspectItemType2ViewClassName;
    private static final InspectItemViewFactory instance;

    static {
        instance = new InspectItemViewFactory();

        inspectItemType2ViewClassName = new HashMap<EInspectItemType, Class<?>>();
        inspectItemType2ViewClassName.put(EInspectItemType.TEXT, TextInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.TEXTEXT, TextInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.NUMBER, NumberInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.DATE, DateInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.TREE, TreeInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.GEOMETRY, GeometryInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.GEOMETRY_AREA, GeometryAreaInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.DEVICE, DeviceInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.SELECTVALVE, DeviceInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.IMAGE, ImageInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.VIDEO, VideoInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.AUDIO, AudioInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.RADIO, RadioInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.RADIOTXT, RadioTextInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.TOGGLE, ToggleInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CHECKBOX, CheckBoxInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.DROPDOWNLIST, DropDownInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.DROPDOWNLISTEXT, DropDownInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.QRCODE, QRCodeInspectItemViewXtd.class);
        //        inspectItemType2ViewClassName.put(EInspectItemType.ATTACHMENT, AttachmentInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.GROUP, GroupInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CONTACTMEN_SINGLE, ContactInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CONTACTMEN_MULTIPLE, ContactInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CONTACTMEN_SINGLE_PROJECT, Contact4ProjectInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT, Contact4ProjectInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.DIVIDER_THICK, DividerThickInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.WORKORDER_CODE, WorkOrderCodeInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.PHONE, PhoneInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.ATTACHMENT, AttachmentViewItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.ATTACHMENT_UPLOAD, AttachmentViewItemUploadXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.SECTION_TITLE, SectionTitleInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.NAMEGEOM, PumpInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.CORS, CorsInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.LINKDROPDOWNLIST, LinkDropDownInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.MATERIAL, MaterialInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.TABLE, TextInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.ORG, OrganisationInspectItemViewXtd.class);
        inspectItemType2ViewClassName.put(EInspectItemType.ORGM, OrganisationMultiSelectionInspectItemViewXtd.class);
    }

    public static InspectItemViewFactory getInstance() {
        return instance;
    }

    public ABaseInspectItemView getInspectItemView(EInspectItemType itemType) {
        Class<?> itemViewClass = inspectItemType2ViewClassName.get(itemType);
        ABaseInspectItemView itemView;
        try {
            itemView = (ABaseInspectItemView) itemViewClass.newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(this, e);
            return null;
        } catch (IllegalAccessException e) {
            LogUtil.e(this, e);
            return null;
        }

        return itemView;
    }
}
