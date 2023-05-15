package org.wordpress.android.ui.reader.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import org.wordpress.android.models.ReaderTag
import org.wordpress.android.models.ReaderTagList
import org.wordpress.android.models.ReaderTagType.BOOKMARKED
import org.wordpress.android.models.ReaderTagType.DEFAULT
import org.wordpress.android.models.ReaderTagType.FOLLOWED
import org.wordpress.android.ui.FilteredRecyclerView

@RunWith(MockitoJUnitRunner::class)
class ReaderUtilsTest {
    @Mock
    lateinit var currentTag: ReaderTag

    @Mock
    lateinit var filteredRecyclerView: FilteredRecyclerView

    private fun getShuffledTagList(): ReaderTagList {
        val tagList = ReaderTagList()
        tagList.addAll(
            listOf(
                ReaderTag("", "", "", ReaderTag.LIKED_PATH, DEFAULT),
                ReaderTag("", "", "", "https://genericendpoint2.com", FOLLOWED),
                ReaderTag("", "", "", ReaderTag.DISCOVER_PATH, DEFAULT),
                ReaderTag("", "", "", "https://genericendpoint4.com", DEFAULT),
                ReaderTag("", "", "", "", BOOKMARKED),
                ReaderTag("", "", "", ReaderTag.FOLLOWING_PATH, DEFAULT),
                ReaderTag("", "", "", "https://genericendpoint7.com", DEFAULT)
            )
        )

        return tagList
    }

    private fun getExpectedTagList(): ReaderTagList {
        val tagList = ReaderTagList()
        tagList.addAll(
            listOf(
                ReaderTag("", "", "", ReaderTag.FOLLOWING_PATH, DEFAULT),
                ReaderTag("", "", "", ReaderTag.DISCOVER_PATH, DEFAULT),
                ReaderTag("", "", "", ReaderTag.LIKED_PATH, DEFAULT),
                ReaderTag("", "", "", "", BOOKMARKED),
                ReaderTag("", "", "", "https://genericendpoint2.com", FOLLOWED),
                ReaderTag("", "", "", "https://genericendpoint4.com", DEFAULT),
                ReaderTag("", "", "", "https://genericendpoint7.com", DEFAULT)
            )
        )

        return tagList
    }

    @Test
    fun `getOrderedTagsList return the desired ordered list`() {
        val shuffledList = getShuffledTagList()
        val orederList = ReaderUtils.getOrderedTagsList(shuffledList, ReaderUtils.getDefaultTagInfo())
        assertThat(orederList).isEqualTo(getExpectedTagList())
    }

    @Test
    fun `isFollowing is based on currentTag status if is not top level reader`() {
        whenever(currentTag.isFollowedSites).thenReturn(true)
        assertThat(ReaderUtils.isTagManagedInFollowingTab(currentTag, false, null)).isEqualTo(true)
        whenever(currentTag.isFollowedSites).thenReturn(false)
        assertThat(ReaderUtils.isTagManagedInFollowingTab(currentTag, false, null)).isEqualTo(false)
    }

    @Test
    fun `isFollowing is based on FilteredRecyclerView when is top level reader`() {
        whenever(currentTag.isFollowedSites).thenReturn(true)
        whenever(filteredRecyclerView.currentFilter).thenReturn(currentTag)
        whenever(filteredRecyclerView.isValidFilter(currentTag)).thenReturn(true)
        var result = ReaderUtils.isTagManagedInFollowingTab(currentTag, true, filteredRecyclerView)
        assertThat(result).isEqualTo(true)

        whenever(currentTag.isFollowedSites).thenReturn(false)
        whenever(filteredRecyclerView.currentFilter).thenReturn(currentTag)
        result = ReaderUtils.isTagManagedInFollowingTab(currentTag, true, filteredRecyclerView)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `when blogId == feedId then this is a feed`() {
        val feedId: Long = 100
        val blogId: Long = 100
        val result = ReaderUtils.isExternalFeed(blogId, feedId)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `when blogId == 0 and feedId is not equal to 0 then this is a feed`() {
        val feedId: Long = 100
        val blogId: Long = 0
        val result = ReaderUtils.isExternalFeed(blogId, feedId)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `when blogId is != 0 and feedId !=0 this is not a feed`() {
        val feedId: Long = 100
        val blogId: Long = 150
        val result = ReaderUtils.isExternalFeed(blogId, feedId)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `when blogId is != 0 and feedId ==0 this is not a feed`() {
        val feedId: Long = 0
        val blogId: Long = 150
        val result = ReaderUtils.isExternalFeed(blogId, feedId)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `given valid url encoded string, when sanitize string is invoked, then string is not sanitized`() {
        val urlEncodedString = "%e7%be%8e%e9%a3%9f"
        val result = ReaderUtils.sanitizeWithDashes(urlEncodedString)
        assertThat(result).isEqualTo(urlEncodedString)
    }

    @Test
    fun `given string with spaces, when sanitize string is invoked, then string is sanitized`() {
        val stringWithSpaces = "string with spaces"
        val result = ReaderUtils.sanitizeWithDashes(stringWithSpaces)
        assertThat(result).isEqualTo("string-with-spaces")
    }

    @Test
    fun `given non-alphanum string without url encoding, when sanitize string is invoked, then string is sanitized `() {
        val nonUrlEncodedString = "non%url*encoded<?string"
        val result = ReaderUtils.sanitizeWithDashes(nonUrlEncodedString)
        assertThat(result).isEqualTo("nonurlencodedstring")
    }

    @Test
    fun `given valid reader tag link, when isTagUrl is invoked, then returns true`() {
        val tagLink = "https://wordpress.com/tag/dailyprompt-123"
        val result = ReaderUtils.isTagUrl(tagLink)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `given invalid reader tag link, when isTagUrl is invoked, then returns false`() {
        val tagLink = "https://wordpress.com/tag/dailyprompt-123/abc"
        val result = ReaderUtils.isTagUrl(tagLink)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `given valid reader tag link, when getTagFromTagUrl is invoked, then returns only tag slug`() {
        val tagLink = "https://wordpress.com/tag/dailyprompt-123"
        val result = ReaderUtils.getTagFromTagUrl(tagLink)
        assertThat(result).isEqualTo("dailyprompt-123")
    }

    @Test
    fun `given invalid reader tag link, when isTagUrl is invoked, then returns empty string`() {
        val tagLink = "https://wordpress.com/tag/dailyprompt-123/abc"
        val result = ReaderUtils.getTagFromTagUrl(tagLink)
        assertThat(result).isEqualTo("")
    }
}
