package com.cxw.cxwproject.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cxw.cxwproject.bean.ContactsModel;

import android.annotation.SuppressLint;
import android.widget.SectionIndexer;

public class MySectionIndexer<T> implements SectionIndexer {

	private static int OTHER_INDEX = 0;
	private List<String> getLetter = new ArrayList<String>();
	private int[] mPositions;
	private int mCount;

	public MySectionIndexer(List<T> citys, List<String> mgetletter) {
		mCount = citys.size();
		getLetter = mgetletter;
		initPositions(citys, getLetter);
	}

	/**
	 * 得到字符串的第一个字母
	 *
	 * @param indexableItem
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public int getSectionIndex(String indexableItem) {
		if (indexableItem == null) {
			return OTHER_INDEX;
		}

		indexableItem = indexableItem.trim();
		String firstLetter = "A";

		if (indexableItem.length() == 0) {
			return OTHER_INDEX;
		} else {
			// get the first letter
			firstLetter = String.valueOf(indexableItem.charAt(0)).toUpperCase();
		}

		int sectionCount = getLetter.size();
		for (int i = 0; i < sectionCount; i++) {
			if (getLetter.get(i).toString().equals(firstLetter)) {
				return i;
			}
		}

		return OTHER_INDEX;

	}

	public void initPositions(List<T> contacts, List<String> getLetter) {
		int sectionCount = getLetter.size();
		mPositions = new int[sectionCount];
		Arrays.fill(mPositions, -1);
		int itemIndex = 0;
		for (T contact : contacts) {
			String indexableItem = ((ContactsModel) contact).getFirstLetter();
			int sectionIndex = getSectionIndex(indexableItem); // find out which

			if (mPositions[sectionIndex] == -1) { // if not set before, then do
				mPositions[sectionIndex] = itemIndex;

			}
			itemIndex++;
		}
		int lastPos = -1;
		for (int i = sectionCount - 1; i >= 0; i--) {
			if (mPositions[i] == -1) {
				mPositions[i] = lastPos;
			}
			lastPos = mPositions[i];

		}
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= getLetter.size()) {
			return -1;
		}
		return mPositions[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= mCount) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions, position);
		// 注意这个方法的返回值，它就是index<0时，返回-index-2的原因
		// 解释Arrays.binarySearch，如果搜索结果在数组中，刚返回它在数组中的索引，如果不在，刚返回第一个比它大的索引的负数-1
		// 如果没弄明白，请自己想查看api
		return index >= 0 ? index : -index - 2;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

}
