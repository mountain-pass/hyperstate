package au.com.mountainpass.hyperstate.core;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Relationship {

  // sourced from
  // http://www.iana.org/assignments/link-relations/link-relations.xhtml on
  // 2015/09/14

  public static final String ABOUT = "about"; // Refers to a resource that is
                                              // the subject of the link's
                                              // context. [RFC6903], section 2
  public static final String ALTERNATE = "alternate"; // Refers to a
                                                      // substitute for this
                                                      // context
                                                      // [http://www.w3.org/TR/html5/links.html#link-type-alternate]
  public static final String APPENDIX = "appendix"; // Refers to an appendix.
                                                    // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String ARCHIVES = "archives"; // Refers to a collection
                                                    // of records, documents,
                                                    // or other materials of
                                                    // historical interest.
                                                    // [http://www.w3.org/TR/2011/WD-html5-20110113/links.html#rel-archives]
  public static final String AUTHOR = "author"; // Refers to the context's
                                                // author.
                                                // [http://www.w3.org/TR/html5/links.html#link-type-author]
  public static final String BOOKMARK = "bookmark"; // Gives a permanent link
                                                    // to use for bookmarking
                                                    // purposes.
                                                    // [http://www.w3.org/TR/html5/links.html#link-type-bookmark]
  public static final String CANONICAL = "canonical"; // Designates the
                                                      // preferred version of
                                                      // a resource (the IRI
                                                      // and its contents).
                                                      // [RFC6596]
  public static final String CHAPTER = "chapter"; // Refers to a chapter in a
                                                  // collection of resources.
                                                  // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String COLLECTION = "collection"; // The target IRI
                                                        // points to a
                                                        // resource which
                                                        // represents the
                                                        // collection resource
                                                        // for the context
                                                        // IRI. [RFC6573]
  public static final String CONTENTS = "contents"; // Refers to a table of
                                                    // contents.
                                                    // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String COPYRIGHT = "copyright"; // Refers to a copyright
                                                      // statement that
                                                      // applies to the link's
                                                      // context.
                                                      // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String CREATE_FORM = "create-form"; // The target IRI
                                                          // points to a
                                                          // resource where a
                                                          // submission form
                                                          // can
                                                          // be obtained.
                                                          // [RFC6861]
  public static final String CURRENT = "current"; // Refers to a resource
                                                  // containing the most
                                                  // recent item(s) in a
                                                  // collection of resources.
                                                  // [RFC5005]
  public static final String DERIVEDFROM = "derivedfrom"; // The target IRI
                                                          // points to a
                                                          // resource from
                                                          // which this
                                                          // material was
                                                          // derived.
                                                          // [draft-hoffman-xml2rfc]
  public static final String DESCRIBEDBY = "describedby"; // Refers to a
                                                          // resource
                                                          // providing
                                                          // information about
                                                          // the link's
                                                          // context.
                                                          // [http://www.w3.org/TR/powder-dr/#assoc-linking]
  public static final String DESCRIBES = "describes"; // The relationship A
                                                      // 'describes' B asserts
                                                      // that resource A
                                                      // provides a
                                                      // description of
                                                      // resource B. There are
                                                      // no constraints on the
                                                      // format or
                                                      // representation of
                                                      // either A or B,
                                                      // neither are there any
                                                      // further constraints
                                                      // on either resource.
                                                      // [RFC6892] This link
                                                      // relation type is the
                                                      // inverse of the
                                                      // 'describedby'
                                                      // relation type. While
                                                      // 'describedby'
                                                      // establishes a
                                                      // relation from the
                                                      // described resource
                                                      // back to the resource
                                                      // that describes it,
                                                      // 'describes'
                                                      // established a
                                                      // relation from the
                                                      // describing resource
                                                      // to the resource it
                                                      // describes. If B is
                                                      // 'describedby' A, then
                                                      // A 'describes' B.
  public static final String DISCLOSURE = "disclosure"; // Refers to a list of
                                                        // patent disclosures
                                                        // made with respect
                                                        // to material for
                                                        // which 'disclosure'
                                                        // relation is
                                                        // specified.
                                                        // [RFC6579]
  public static final String DUPLICATE = "duplicate"; // Refers to a resource
                                                      // whose available
                                                      // representations are
                                                      // byte-for-byte
                                                      // identical with the
                                                      // corresponding
                                                      // representations of
                                                      // the context IRI.
                                                      // [RFC6249] This
                                                      // relation is for
                                                      // static resources.
                                                      // That is, an HTTP GET
                                                      // request on any
                                                      // duplicate will return
                                                      // the same
                                                      // representation. It
                                                      // does not make sense
                                                      // for dynamic or
                                                      // POSTable resources
                                                      // and should not be
                                                      // used for them.
  public static final String EDIT = "edit"; // Refers to a resource that can
                                            // be used to edit the link's
                                            // context. [RFC5023]
  public static final String EDIT_FORM = "edit-form"; // The target IRI points
                                                      // to a resource where a
                                                      // submission form for
                                                      // editing associated
                                                      // resource can be
                                                      // obtained. [RFC6861]
  public static final String EDIT_MEDIA = "edit-media"; // Refers to a
                                                        // resource
                                                        // that can be used to
                                                        // edit media
                                                        // associated
                                                        // with the link's
                                                        // context. [RFC5023]
  public static final String ENCLOSURE = "enclosure"; // Identifies a related
                                                      // resource that is
                                                      // potentially large and
                                                      // might require special
                                                      // handling. [RFC4287]
  public static final String FIRST = "first"; // An IRI that refers to the
                                              // furthest preceding resource
                                              // in a series of resources.
                                              // [RFC5988] This relation type
                                              // registration did not indicate
                                              // a reference. Originally
                                              // requested by Mark Nottingham
                                              // in December 2004.
  public static final String GLOSSARY = "glossary"; // Refers to a glossary of
                                                    // terms.
                                                    // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String HELP = "help"; // Refers to context-sensitive
                                            // help.
                                            // [http://www.w3.org/TR/html5/links.html#link-type-help]
  public static final String HOSTS = "hosts"; // Refers to a resource hosted
                                              // by the server indicated by
                                              // the link context. [RFC6690]
                                              // This relation is used in CoRE
                                              // where links are retrieved as
                                              // a "/.well-known/core"
                                              // resource representation, and
                                              // is the default relation type
                                              // in the CoRE Link Format.
  public static final String HUB = "hub"; // Refers to a hub that enables
                                          // registration for notification of
                                          // updates to the context.
                                          // [http://pubsubhubbub.googlecode.com]
                                          // This relation type was requested
                                          // by Brett Slatkin.
  public static final String ICON = "icon"; // Refers to an icon representing
                                            // the link's context.
                                            // [http://www.w3.org/TR/html5/links.html#link-type-icon]
  public static final String INDEX = "index"; // Refers to an index.
                                              // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String ITEM = "item"; // The target IRI points to a
                                            // resource that is a member of
                                            // the collection represented by
                                            // the context IRI. [RFC6573]
  public static final String LAST = "last"; // An IRI that refers to the
                                            // furthest following resource in
                                            // a series of resources.
                                            // [RFC5988] This relation type
                                            // registration did not indicate a
                                            // reference. Originally requested
                                            // by Mark Nottingham in December
                                            // 2004.
  public static final String LATEST_VERSION = "latest-version"; // Points to a
                                                                // resource
                                                                // containing
                                                                // the latest
                                                                // (e.g.,
                                                                // current)
                                                                // version of
                                                                // the
                                                                // context.
                                                                // [RFC5829]
  public static final String LICENSE = "license"; // Refers to a license
                                                  // associated with this
                                                  // context. [RFC4946] For
                                                  // implications of use in
                                                  // HTML, see:
                                                  // http://www.w3.org/TR/html5/links.html#link-type-license
  public static final String LRDD = "lrdd"; // Refers to further information
                                            // about the link's context,
                                            // expressed as a LRDD
                                            // ("Link-based Resource
                                            // Descriptor Document") resource.
                                            // See [RFC6415] for information
                                            // about processing this relation
                                            // type in host-meta documents.
                                            // When used elsewhere, it refers
                                            // to additional links and other
                                            // metadata. Multiple instances
                                            // indicate additional LRDD
                                            // resources. LRDD resources MUST
                                            // have an "application/xrd+xml"
                                            // representation, and MAY have
                                            // others. [RFC6415]
  public static final String MEMENTO = "memento"; // The Target IRI points to
                                                  // a Memento, a fixed
                                                  // resource that will not
                                                  // change state anymore.
                                                  // [RFC7089] A Memento for
                                                  // an Original Resource is a
                                                  // resource that
                                                  // encapsulates a prior
                                                  // state of the Original
                                                  // Resource.
  public static final String MONITOR = "monitor"; // Refers to a resource that
                                                  // can be used to monitor
                                                  // changes in an HTTP
                                                  // resource. [RFC5989]
  public static final String MONITOR_GROUP = "monitor-group"; // Refers to a
                                                              // resource that
                                                              // can be used
                                                              // to
                                                              // monitor
                                                              // changes
                                                              // in a
                                                              // specified
                                                              // group of HTTP
                                                              // resources.
                                                              // [RFC5989]
  public static final String NEXT = "next"; // Indicates that the link's
                                            // context is a part of a series,
                                            // and that the next in the series
                                            // is the link target.
                                            // [http://www.w3.org/TR/html5/links.html#link-type-next]
  public static final String NEXT_ARCHIVE = "next-archive"; // Refers to the
                                                            // immediately
                                                            // following
                                                            // archive
                                                            // resource.
                                                            // [RFC5005]
  public static final String NOFOLLOW = "nofollow"; // Indicates that the
                                                    // context’s original
                                                    // author or publisher
                                                    // does not endorse the
                                                    // link target.
                                                    // [http://www.w3.org/TR/html5/links.html#link-type-nofollow]
  public static final String NOREFERRER = "noreferrer"; // Indicates that no
                                                        // referrer
                                                        // information is to
                                                        // be leaked when
                                                        // following the link.
                                                        // [http://www.w3.org/TR/html5/links.html#link-type-noreferrer]
  public static final String ORIGINAL = "original"; // The Target IRI points
                                                    // to an Original
                                                    // Resource. [RFC7089] An
                                                    // Original Resource is a
                                                    // resource that exists or
                                                    // used to exist, and for
                                                    // which access to one of
                                                    // its prior states may be
                                                    // required.
  public static final String PAYMENT = "payment"; // Indicates a resource
                                                  // where payment is
                                                  // accepted. [RFC5988] This
                                                  // relation type
                                                  // registration did not
                                                  // indicate a reference.
                                                  // Requested by Joshua
                                                  // Kinberg and Robert Sayre.
                                                  // It is meant as a general
                                                  // way to facilitate acts of
                                                  // payment, and thus this
                                                  // specification makes no
                                                  // assumptions on the type
                                                  // of payment or transaction
                                                  // protocol. Examples may
                                                  // include a web page where
                                                  // donations are accepted or
                                                  // where goods and services
                                                  // are available for
                                                  // purchase. rel="payment"
                                                  // is not intended to
                                                  // initiate an automated
                                                  // transaction. In Atom
                                                  // documents, a link element
                                                  // with a rel="payment"
                                                  // attribute may exist at
                                                  // the feed/channel level
                                                  // and/or the entry/item
                                                  // level. For example, a
                                                  // rel="payment" link at the
                                                  // feed/channel level may
                                                  // point to a "tip jar" URI,
                                                  // whereas an entry/ item
                                                  // containing a book review
                                                  // may include a
                                                  // rel="payment" link that
                                                  // points to the location
                                                  // where the book may be
                                                  // purchased through an
                                                  // online retailer.
  public static final String PREDECESSOR_VERSION = "predecessor-version"; // Points
                                                                          // to
                                                                          // a
                                                                          // resource
                                                                          // containing
                                                                          // the
                                                                          // predecessor
                                                                          // version
                                                                          // in
                                                                          // the
                                                                          // version
                                                                          // history.
                                                                          // [RFC5829]
  public static final String PREFETCH = "prefetch"; // Indicates that the link
                                                    // target should be
                                                    // preemptively cached.
                                                    // [http://www.w3.org/TR/html5/links.html#link-type-prefetch]
  public static final String PREV = "prev"; // Indicates that the link's
                                            // resource in an ordered
                                            // series of resources.
                                            // Synonym for "prev".
                                            // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String PREV_ARCHIVE = "prev-archive"; // Refers to the
  // context is a part of a series,
  // and that the previous in the
  // series is the link target.
  // [http://www.w3.org/TR/html5/links.html#link-type-prev]
  public static final String PREVIEW = "preview"; // Refers to a resource that
                                                  // provides a preview of the
                                                  // link's context.
                                                  // [RFC6903], section 3
  public static final String PREVIOUS = "previous"; // Refers to the previous
                                                    // immediately
                                                    // preceding
                                                    // archive
                                                    // resource.
                                                    // [RFC5005]
  public static final String PRIVACY_POLICY = "privacy-policy"; // Refers to a
                                                                // privacy
                                                                // policy
                                                                // associated
                                                                // with the
                                                                // link's
                                                                // context.
                                                                // [RFC6903],
                                                                // section 4
  public static final String PROFILE = "profile"; // Identifying that a
                                                  // resource representation
                                                  // conforms to a certain
                                                  // profile, without
                                                  // affecting the non-profile
                                                  // semantics of the resource
                                                  // representation. [RFC6906]
                                                  // Profile URIs are
                                                  // primarily intended to be
                                                  // used as identifiers, and
                                                  // thus clients SHOULD NOT
                                                  // indiscriminately access
                                                  // profile URIs.
  public static final String RELATED = "related"; // Identifies a related
                                                  // resource. [RFC4287]
  public static final String REPLIES = "replies"; // Identifies a resource
                                                  // that is a reply to the
                                                  // context of the link.
                                                  // [RFC4685]
  public static final String SEARCH = "search"; // Refers to a resource that
                                                // can be used to search
                                                // through the link's context
                                                // and related resources.
                                                // [http://www.opensearch.org/Specifications/OpenSearch/1.1]
  public static final String SECTION = "section"; // Refers to a section in a
                                                  // collection of resources.
                                                  // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String SELF = "self"; // Conveys an identifier for the
                                            // link's context. [RFC4287]
  public static final String SERVICE = "service"; // Indicates a URI that can
                                                  // be used to retrieve a
                                                  // service document.
                                                  // [RFC5023] When used in an
                                                  // Atom document, this
                                                  // relation type specifies
                                                  // Atom Publishing Protocol
                                                  // service documents by
                                                  // default. Requested by
                                                  // James Snell.
  public static final String START = "start"; // Refers to the first resource
                                              // in a collection of resources.
                                              // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String STYLESHEET = "stylesheet"; // Refers to a
                                                        // stylesheet.
                                                        // [http://www.w3.org/TR/html5/links.html#link-type-stylesheet]
  public static final String SUBSECTION = "subsection"; // Refers to a
                                                        // resource serving as
                                                        // a subsection in a
                                                        // collection of
                                                        // resources.
                                                        // [http://www.w3.org/TR/1999/REC-html401-19991224]
  public static final String SUCCESSOR_VERSION = "successor-version"; // Points
                                                                      // to a
                                                                      // resource
                                                                      // containing
                                                                      // the
                                                                      // successor
                                                                      // version
                                                                      // in
                                                                      // the
                                                                      // version
                                                                      // history.
                                                                      // [RFC5829]
  public static final String TAG = "tag"; // Gives a tag (identified by the
                                          // given address) that applies to
                                          // the current document.
                                          // [http://www.w3.org/TR/html5/links.html#link-type-tag]
  public static final String TERMS_OF_SERVICE = "terms-of-service"; // Refers
                                                                    // to
                                                                    // the
                                                                    // terms
                                                                    // of
                                                                    // service
                                                                    // associated
                                                                    // with
                                                                    // the
                                                                    // link's
                                                                    // context.
                                                                    // [RFC6903],
                                                                    // section
                                                                    // 5
  public static final String TIMEGATE = "timegate"; // The Target IRI points
                                                    // to a TimeGate for an
                                                    // Original Resource.
                                                    // [RFC7089] A TimeGate
                                                    // for an Original
                                                    // Resource is a resource
                                                    // that is capable of
                                                    // datetime negotiation to
                                                    // support access to prior
                                                    // states of the Original
                                                    // Resource.
  public static final String TIMEMAP = "timemap"; // The Target IRI points to
                                                  // a TimeMap for an Original
                                                  // Resource. [RFC7089] A
                                                  // TimeMap for an Original
                                                  // Resource is a resource
                                                  // from which a list of URIs
                                                  // of Mementos of the
                                                  // Original Resource is
                                                  // available.
  public static final String TYPE = "type"; // Refers to a resource
                                            // identifying the abstract
                                            // semantic type of which the
                                            // link's context is considered to
                                            // be an instance. [RFC6903],
                                            // section 6
  public static final String UP = "up"; // Refers to a parent document in a
                                        // hierarchy of documents. [RFC5988]
                                        // This relation type registration did
                                        // not indicate a reference. Requested
                                        // by Noah Slater.
  public static final String VERSION_HISTORY = "version-history"; // Points to
                                                                  // a
                                                                  // resource
                                                                  // containing
                                                                  // the
                                                                  // version
                                                                  // history
                                                                  // for
                                                                  // the
                                                                  // context.
                                                                  // [RFC5829]
  public static final String VIA = "via"; // Identifies a resource that is the
                                          // source of the information in the
                                          // link's context. [RFC4287]
  public static final String WORKING_COPY = "working-copy"; // Points to a
                                                            // working copy
                                                            // for
                                                            // this resource.
                                                            // [RFC5829]
  public static final String WORKING_COPY_OF = "working-copy-of"; // Points to
                                                                  // the
                                                                  // versioned
                                                                  // resource
                                                                  // from
                                                                  // which
                                                                  // this
                                                                  // working
                                                                  // copy was
                                                                  // obtained.
                                                                  // [RFC5829]

  // at least one. How to enforce this?
  private final String[] natures;

  public Relationship(final String... natures) {
    this.natures = natures;
  }

  /**
   * @return the nature
   */
  @JsonProperty("rel")
  public String[] getNature() {
    return natures;
  }

  public boolean hasNature(final String nature) {
    return Arrays.asList(natures).contains(nature);
  }
}
